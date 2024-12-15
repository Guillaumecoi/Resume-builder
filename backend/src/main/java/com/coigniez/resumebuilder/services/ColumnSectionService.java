package com.coigniez.resumebuilder.services;

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

    //TODO: Make sure the order is correct
    //TODO: Authentication
    
    public Long create(ColumnSectionRequest request) {
        ColumnSection columnSection = columnSectionMapper.toEntity(request);

        // Set the column
        Column column = columnRepository.findById(request.getColumnId())
                .orElseThrow(() -> new EntityNotFoundException("Column not found"));
        column.addSectionMapping(columnSection);

        // Set the section
        Section section = sectionRepository.findById(request.getSectionId())
                .orElseThrow(() -> new EntityNotFoundException("Section not found"));
        section.addColumnSection(columnSection);

        return columnSectionRepository.save(columnSection).getId();
    }

    public ColumnSectionResponse get(long id) {
        ColumnSection columnSection = columnSectionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ColumnSection not found"));
        return columnSectionMapper.toDto(columnSection);
    }

    public void update(ColumnSectionRequest request) {
        if (request.getId() == null) {
            throw new IllegalArgumentException("ColumnSection id is required for update");
        }
        long id = request.getId();

        ColumnSection columnSection = columnSectionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ColumnSection not found"));
        // Update theexistingColumnSection entity
        columnSectionMapper.updateEntity(columnSection, request);
        // Save the updated entity
        columnSectionRepository.save(columnSection);
    }

    public void delete(long id) {
        ColumnSection columnSection = columnSectionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ColumnSection not found"));
        columnSectionRepository.delete(columnSection);
    }
    
}