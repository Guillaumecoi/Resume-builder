package com.coigniez.resumebuilder.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.coigniez.resumebuilder.domain.column.Column;
import com.coigniez.resumebuilder.domain.column.ColumnRepository;
import com.coigniez.resumebuilder.domain.columnsection.ColumnSection;
import com.coigniez.resumebuilder.domain.columnsection.ColumnSectionMapper;
import com.coigniez.resumebuilder.domain.columnsection.ColumnSectionRepository;
import com.coigniez.resumebuilder.domain.columnsection.ColumnSectionRequest;
import com.coigniez.resumebuilder.domain.columnsection.ColumnSectionResponse;
import com.coigniez.resumebuilder.domain.section.Section;
import com.coigniez.resumebuilder.domain.section.SectionRepository;
import com.coigniez.resumebuilder.interfaces.CrudService;
import com.coigniez.resumebuilder.util.SecurityUtils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Transactional
@Service
public class ColumnSectionService implements CrudService<ColumnSectionResponse, ColumnSectionRequest> {

    private final ColumnSectionRepository columnSectionRepository;
    private final ColumnRepository columnRepository;
    private final SectionRepository sectionRepository;
    private final ColumnSectionMapper columnSectionMapper;
    private final SecurityUtils securityUtils;
    private final EntityManager entityManager;
    
    public Long create(ColumnSectionRequest request) {
        // Check if the user has access to the column and section
        hasAccessColumn(request.getColumnId());
        hasAccessSection(request.getSectionId());

        // Get the column and section
        Column column = columnRepository.findById(request.getColumnId())
                .orElseThrow(() -> new EntityNotFoundException("Column not found"));
        Section section = sectionRepository.findById(request.getSectionId())
                .orElseThrow(() -> new EntityNotFoundException("Section not found"));

        // Check if the column and section belong to the same resume
        if(column.getLayout().getResume().getId() != section.getResume().getId()) {
            throw new IllegalArgumentException("Column and Section must belong to the same resume");
        }

        // Find the maximum sectionOrder in the column
        int maxOrder = columnSectionRepository.findMaxSectionOrderByColumnId(column.getId()).orElse(0);
        int newOrder = request.getSectionOrder() == null ? maxOrder + 1 : request.getSectionOrder();

        // Shift the order
        incrementSectionOrder(column.getId(), newOrder, maxOrder + 1);

        // Create the entity from the request
        request.setId(null);
        request.setSectionOrder(newOrder);
        ColumnSection columnSection = columnSectionMapper.toEntity(request);

        // Add the columnSection to the column and section
        column.addSectionMapping(columnSection);
        section.addColumnSection(columnSection);
        
        // Save the columnSection
        return columnSectionRepository.save(columnSection).getId();
    }

    public ColumnSectionResponse get(long id) {
        // Check if the user has access to this columnSection
        hasAccess(id);

        // Get the existing columnSection entity
        ColumnSection columnSection = columnSectionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ColumnSection not found"));
        return columnSectionMapper.toDto(columnSection);
    }

    public void update(ColumnSectionRequest request) {
        // Check if the id is provided
        if (request.getId() == null) {
            throw new IllegalArgumentException("ColumnSection id is required for update");
        }
        long id = request.getId();

        // Check if the user has access to this columnSection
        hasAccess(id);

        // Get the existing columnSection entity
        ColumnSection columnSection = columnSectionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ColumnSection not found"));
        
        // Shift the order
        if (!request.getSectionOrder().equals(columnSection.getSectionOrder())) {
            if (request.getSectionOrder() > columnSection.getSectionOrder()) {
                decrementSectionOrder(columnSection.getColumn().getId(), request.getSectionOrder(), columnSection.getSectionOrder());
            } else {
                incrementSectionOrder(columnSection.getColumn().getId(), request.getSectionOrder(), columnSection.getSectionOrder());
            }
        }

        // Update theexistingColumnSection entity
        columnSectionMapper.updateEntity(columnSection, request);
        
        // Save the updated entity
        columnSectionRepository.save(columnSection);
    }

    public void delete(long id) {
        // Check if the user has access to this columnSection
        hasAccess(id);

        // Get the existing columnSection entity
        ColumnSection columnSection = columnSectionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ColumnSection not found"));

        // Remove the columnSection from the column and section
        Column column = columnSection.getColumn();        
        Section section = columnSection.getSection();

        column.removeSectionMapping(columnSection);
        section.removeColumnSection(columnSection);
        
        // Delete the columnSection
        columnSectionRepository.delete(columnSection);

        // Shift other columnSections
        int maxOrder = columnSectionRepository.findMaxSectionOrderByColumnId(column.getId()).orElse(0);
        decrementSectionOrder(column.getId(), maxOrder + 1, columnSection.getSectionOrder());
    }

    public List<ColumnSectionResponse> getAllByColumnId(long id) {
        // Check if the user has access to the column
        hasAccessColumn(id);

        // Get all columnSections and map them to DTOs
        return columnSectionRepository.findAllByColumnId(id).stream()
                .map(columnSectionMapper::toDto)
                .toList();
    }

    /**
     * Shift the order of the columnSections in the column.
     * All other columnSections with order >= newOrder and < oldOrder will be incremented.
     * 
     * @param columnId the id of the column
     * @param newOrder the new order of the columnSection
     * @param oldOrder the old order of the columnSection
     */
    private void incrementSectionOrder(long columnId, int newOrder, int oldOrder) {
        columnSectionRepository.incrementSectionOrderBetween(columnId, newOrder, oldOrder);
        refreshSectionOrder(columnId);
    }

    /**
     * Shift the order of the columnSections in the column.
     * All other columnSections with order > oldOrder and <= newOrder will be decremented.
     * 
     * @param columnId the id of the column
     * @param newOrder the new order of the columnSection
     * @param oldOrder the old order of the columnSection
     */
    private void decrementSectionOrder(long columnId, int newOrder, int oldOrder) {
        columnSectionRepository.decrementSectionOrderBetween(columnId, newOrder, oldOrder);
        refreshSectionOrder(columnId);
    }

    /**
     * Refresh the order of the columnSections in the column.
     * 
     * @param columnId the id of the column
     */
    private void refreshSectionOrder(long columnId) {
        List<ColumnSection> columnSections = columnSectionRepository.findAllByColumnId(columnId);
        for (ColumnSection columnSection : columnSections) {
            entityManager.refresh(columnSection);
        }
    }

    /**
     * Check if the user has access to the columnSection.
     * 
     * @param id the id of the columnSection
     */
    private void hasAccess(long id) {
        String owner = columnSectionRepository.findCreatedBy(id)
                .orElseThrow(() -> new EntityNotFoundException("ColumnSection not found"));
        securityUtils.hasAccess(List.of(owner));
    }

    /**
     * Check if the user has access to the column.
     * 
     * @param id the id of the column
     */
    private void hasAccessColumn(long id) {
        String owner = columnRepository.findCreatedBy(id)
                .orElseThrow(() -> new EntityNotFoundException("Column not found"));
        securityUtils.hasAccess(List.of(owner));
    }

    /**
     * Check if the user has access to the section.
     * 
     * @param id the id of the section
     */
    private void hasAccessSection(long id) {
        String owner = sectionRepository.findCreatedBy(id)
                .orElseThrow(() -> new EntityNotFoundException("Section not found"));
        securityUtils.hasAccess(List.of(owner));
    }
    
}
