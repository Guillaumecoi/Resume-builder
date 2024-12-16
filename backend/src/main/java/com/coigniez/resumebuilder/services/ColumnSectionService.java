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

    //TODO: Make sure the order is correct
    
    public Long create(ColumnSectionRequest request) {
        hasAccessColumn(request.getColumnId());
        hasAccessSection(request.getSectionId());

        ColumnSection columnSection = columnSectionMapper.toEntity(request);

        // Set the column
        Column column = columnRepository.findById(request.getColumnId())
                .orElseThrow(() -> new EntityNotFoundException("Column not found"));

        // Set the section
        Section section = sectionRepository.findById(request.getSectionId())
                .orElseThrow(() -> new EntityNotFoundException("Section not found"));

        if(column.getLayout().getResume().getId() != section.getResume().getId()) {
            throw new IllegalArgumentException("Column and Section must belong to the same resume");
        }

        column.addSectionMapping(columnSection);
        section.addColumnSection(columnSection);

        return columnSectionRepository.save(columnSection).getId();
    }

    public ColumnSectionResponse get(long id) {
        hasAccess(id);

        ColumnSection columnSection = columnSectionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ColumnSection not found"));
        return columnSectionMapper.toDto(columnSection);
    }

    public void update(ColumnSectionRequest request) {
        if (request.getId() == null) {
            throw new IllegalArgumentException("ColumnSection id is required for update");
        }
        long id = request.getId();
        hasAccess(id);

        ColumnSection columnSection = columnSectionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ColumnSection not found"));
        // Update theexistingColumnSection entity
        columnSectionMapper.updateEntity(columnSection, request);
        // Save the updated entity
        columnSectionRepository.save(columnSection);
    }

    public void delete(long id) {
        hasAccess(id);
        ColumnSection columnSection = columnSectionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ColumnSection not found"));

        // Remove the columnSection from the column
        Column column = columnSection.getColumn();
        column.removeSectionMapping(columnSection);

        // Remove the columnSection from the section
        Section section = columnSection.getSection();
        section.removeColumnSection(columnSection);
        
        columnSectionRepository.delete(columnSection);
    }

    private void hasAccess(long id) {
        String owner = columnSectionRepository.findCreatedBy(id)
                .orElseThrow(() -> new EntityNotFoundException("ColumnSection not found"));
        securityUtils.hasAccess(List.of(owner));
    }

    private void hasAccessColumn(long id) {
        String owner = columnRepository.findCreatedBy(id)
                .orElseThrow(() -> new EntityNotFoundException("Column not found"));
        securityUtils.hasAccess(List.of(owner));
    }

    private void hasAccessSection(long id) {
        String owner = sectionRepository.findCreatedBy(id)
                .orElseThrow(() -> new EntityNotFoundException("Section not found"));
        securityUtils.hasAccess(List.of(owner));
    }
    
}
