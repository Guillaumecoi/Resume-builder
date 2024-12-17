package com.coigniez.resumebuilder.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.coigniez.resumebuilder.domain.column.Column;
import com.coigniez.resumebuilder.domain.column.ColumnRepository;
import com.coigniez.resumebuilder.domain.columnsection.ColumnSection;
import com.coigniez.resumebuilder.domain.columnsection.ColumnSectionMapper;
import com.coigniez.resumebuilder.domain.columnsection.ColumnSectionParentType;
import com.coigniez.resumebuilder.domain.columnsection.ColumnSectionRepository;
import com.coigniez.resumebuilder.domain.columnsection.ColumnSectionRequest;
import com.coigniez.resumebuilder.domain.columnsection.ColumnSectionResponse;
import com.coigniez.resumebuilder.domain.section.Section;
import com.coigniez.resumebuilder.domain.section.SectionRepository;
import com.coigniez.resumebuilder.interfaces.MultiParentEntityService;
import com.coigniez.resumebuilder.util.SecurityUtils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ColumnSectionService implements MultiParentEntityService<ColumnSectionRequest, ColumnSectionResponse, Long, ColumnSectionParentType> {

    private final ColumnSectionRepository columnSectionRepository;
    private final ColumnRepository columnRepository;
    private final SectionRepository sectionRepository;
    private final ColumnSectionMapper columnSectionMapper;
    private final SecurityUtils securityUtils;
    private final EntityManager entityManager;
    
    @Override
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
        int maxOrder = columnSectionRepository.findMaxSectionOrderByColumnId(request.getColumnId()).orElse(0);
        int newOrder = request.getSectionOrder() == null ? maxOrder + 1 : request.getSectionOrder();

        // Shift the order
        incrementSectionOrder(request.getColumnId(), newOrder, maxOrder + 1);

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

    @Override
    public ColumnSectionResponse get(Long id) {
        // Check if the user has access to this columnSection
        hasAccess(id);

        // Get the existing columnSection entity
        ColumnSection columnSection = columnSectionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ColumnSection not found"));
        return columnSectionMapper.toDto(columnSection);
    }

    @Override
    public void update(ColumnSectionRequest request) {
        // Check if the user has access to this columnSection
        hasAccess(request.getId());

        // Get the existing columnSection entity
        ColumnSection columnSection = columnSectionRepository.findById(request.getId())
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

    @Override
    public void delete(Long id) {
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

    @Override
    public List<ColumnSectionResponse> getAllByParentId(ColumnSectionParentType parentType, Long parentId) {
        if (parentType == ColumnSectionParentType.COLUMN) {
            // Check if the user has access to the column
            hasAccessColumn(parentId);

            // Get all columnSections in the column
            return columnSectionRepository.findAllByColumnId(parentId).stream()
                    .map(columnSectionMapper::toDto)
                    .toList();

        } else if (parentType == ColumnSectionParentType.SECTION) {
            // Check if the user has access to the section
            hasAccessSection(parentId);

            // Get all columnSections in the section
            return columnSectionRepository.findAllBySectionId(parentId).stream()
                    .map(columnSectionMapper::toDto)
                    .toList();

        } else {
            throw new UnsupportedOperationException(parentType + " is not supported");
        }
    }

    @Override
    public void removeAllByParentId(ColumnSectionParentType parentType, Long parentId) {
        if (parentType == ColumnSectionParentType.COLUMN) {
            // Check if the user has access to the column
            hasAccessColumn(parentId);

            // Remove all columnSections from the column
            columnRepository.findById(parentId)
                    .orElseThrow(() -> new EntityNotFoundException("Column not found"))
                    .clearSectionMappings();

            // Remove all columnSections in the column
            columnSectionRepository.removeAllByColumnId(parentId);

        } else if (parentType == ColumnSectionParentType.SECTION) {
            // Check if the user has access to the section
            hasAccessSection(parentId);

            // Remove all columnSections from the section
            sectionRepository.findById(parentId)
                    .orElseThrow(() -> new EntityNotFoundException("Section not found"))
                    .clearColumnSections();

            // Remove all columnSections in the section
            columnSectionRepository.removeAllBySectionId(parentId);
        } else {
            throw new UnsupportedOperationException(parentType + " is not supported");
        }
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
