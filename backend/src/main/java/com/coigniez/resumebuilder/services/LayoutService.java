package com.coigniez.resumebuilder.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.coigniez.resumebuilder.domain.column.Column;
import com.coigniez.resumebuilder.domain.column.ColumnMapper;
import com.coigniez.resumebuilder.domain.column.ColumnRequest;
import com.coigniez.resumebuilder.domain.layout.Layout;
import com.coigniez.resumebuilder.domain.layout.LayoutMapper;
import com.coigniez.resumebuilder.domain.layout.LayoutRepository;
import com.coigniez.resumebuilder.domain.layout.LayoutRequest;
import com.coigniez.resumebuilder.domain.layout.LayoutResponse;
import com.coigniez.resumebuilder.domain.layout.templates.LayoutTemplate;
import com.coigniez.resumebuilder.domain.resume.Resume;
import com.coigniez.resumebuilder.domain.resume.ResumeRepository;
import com.coigniez.resumebuilder.interfaces.CrudService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Transactional
@Service
public class LayoutService implements CrudService<LayoutResponse, LayoutRequest> {

    private final LayoutRepository layoutRepository;
    private final ResumeRepository resumeRepository;
    private final LayoutMapper layoutMapper;
    private final ColumnMapper columnMapper;
    
    public Long create(Long parentId, LayoutRequest request) {
        Layout layout = layoutMapper.toEntity(request);
        Resume resume = resumeRepository.findById(parentId).orElseThrow(() -> new EntityNotFoundException("Resume not found"));
        resume.addLayout(layout);

        if(layout.getColumns().isEmpty()) {
            List<ColumnRequest> columnRequests = LayoutTemplate.getDefaultColumns(layout.getNumberOfColumns());
            List<Column> mappedColumns = columnRequests.stream()
                .map(columnMapper::toEntity)
                .collect(Collectors.toList());
            layout.setColumns(mappedColumns);
        }

        return layoutRepository.save(layout).getId();
    }

    public LayoutResponse get(Long id) {
        Layout layout = layoutRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Layout not found"));
        return layoutMapper.toDto(layout);
    }

    public void update(Long id, LayoutRequest request) {
        Layout existingLayout = layoutRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Layout not found"));
        Layout updatedLayout = layoutMapper.toEntity(request);

        BeanUtils.copyProperties(updatedLayout, existingLayout, "id", "resume");

        layoutRepository.save(existingLayout);        
    }

    public void delete(Long id) {
        layoutRepository.deleteById(id);
    }

}
