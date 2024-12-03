package com.coigniez.resumebuilder.model.layout.column.ColumnSection;

import org.springframework.stereotype.Service;

import com.coigniez.resumebuilder.model.layout.column.Column;
import com.coigniez.resumebuilder.model.layout.column.ColumnRepository;
import com.coigniez.resumebuilder.model.section.Section;
import com.coigniez.resumebuilder.model.section.SectionRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Transactional
@Service
public class ColumnSectionService {

    private final ColumnSectionRepository columnSectionRepository;
    private final ColumnRepository columnRepository;
    private final SectionRepository sectionRepository;
    private final ColumnSectionMapper columnSectionMapper;

    
    public Long create(Long parentId, ColumnSectionDTO request) {
        ColumnSection columnSection = columnSectionMapper.toEntity(request);

        // Set the column
        Column column = columnRepository.findById(parentId).orElseThrow(() -> new EntityNotFoundException("Column not found"));
        column.addSectionMapping(columnSection);

        // Set the section
        Section section = sectionRepository.findById(request.getSectionId()).orElseThrow(() -> new EntityNotFoundException("Section not found"));
        section.addColumnSection(columnSection);

        return columnSectionRepository.save(columnSection).getId();
    }

    public ColumnSection get(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'get'");
    }

    public void update(Long id, ColumnSectionDTO request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    public void delete(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }
    
}
