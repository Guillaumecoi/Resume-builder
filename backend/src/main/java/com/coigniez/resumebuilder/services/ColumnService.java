package com.coigniez.resumebuilder.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.coigniez.resumebuilder.domain.column.Column;
import com.coigniez.resumebuilder.domain.column.ColumnMapper;
import com.coigniez.resumebuilder.domain.column.ColumnRepository;
import com.coigniez.resumebuilder.domain.column.ColumnRequest;
import com.coigniez.resumebuilder.domain.column.ColumnResponse;
import com.coigniez.resumebuilder.domain.layout.Layout;
import com.coigniez.resumebuilder.domain.layout.LayoutRepository;
import com.coigniez.resumebuilder.interfaces.CrudService;
import com.coigniez.resumebuilder.util.SecurityUtils;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Transactional
@Service
public class ColumnService implements CrudService<ColumnResponse, ColumnRequest> {

    private final ColumnRepository columnRepository;
    private final LayoutRepository layoutRepository;
    private final ColumnMapper columnMapper;
    private final SecurityUtils securityUtils;

    public Long create(ColumnRequest request) {
        hasAccessLayout(request.getLayoutId());

        Column column = columnMapper.toEntity(request);
        Layout layout = layoutRepository.findById(request.getLayoutId()).orElseThrow(() -> new EntityNotFoundException("Layout not found"));
        layout.addColumn(column);
        
        return columnRepository.save(column).getId();
    }

    public ColumnResponse get(long id) {
        hasAccess(id);

        Column column = columnRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Column not found"));
        return columnMapper.toDto(column);
    }

    public void update(ColumnRequest request) {
        hasAccess(request.getId());

        if (request.getId() == null) {
            throw new IllegalArgumentException("Column id is required for update");
        }
        long id = request.getId();

        Column column = columnRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Column not found"));
        // Update the entity
        columnMapper.updateEntity(column, request);
        // Save the updated entity
        columnRepository.save(column);
        
    }

    public void delete(long id) {
        hasAccess(id);
        Column column = columnRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Column not found"));

        Layout layout = column.getLayout();
        layout.removeColumn(column);
        
        columnRepository.deleteById(id);
    }

    private void hasAccess(long id) {
        String owner = columnRepository.findCreatedBy(id)
            .orElseThrow(() -> new EntityNotFoundException("Column not found"));
        securityUtils.hasAccess(List.of(owner));
    }

    private void hasAccessLayout(long id) {
        String owner = layoutRepository.findCreatedBy(id)
            .orElseThrow(() -> new EntityNotFoundException("Layout not found"));
        securityUtils.hasAccess(List.of(owner));
    }
}
