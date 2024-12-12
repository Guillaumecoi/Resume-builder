package com.coigniez.resumebuilder.services;

import org.springframework.stereotype.Service;

import com.coigniez.resumebuilder.domain.column.Column;
import com.coigniez.resumebuilder.domain.column.ColumnMapper;
import com.coigniez.resumebuilder.domain.column.ColumnRepository;
import com.coigniez.resumebuilder.domain.column.ColumnRequest;
import com.coigniez.resumebuilder.domain.column.ColumnResponse;
import com.coigniez.resumebuilder.domain.layout.Layout;
import com.coigniez.resumebuilder.domain.layout.LayoutRepository;
import com.coigniez.resumebuilder.interfaces.CrudService;

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
    

    public Long create(ColumnRequest request) {
        Column column = columnMapper.toEntity(request);
        Layout layout = layoutRepository.findById(request.getLayoutId()).orElseThrow(() -> new EntityNotFoundException("Layout not found"));
        layout.addColumn(column);
        
        return columnRepository.save(column).getId();
    }

    public ColumnResponse get(Long id) {
        Column column = columnRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Column not found"));
        return columnMapper.toDto(column);
    }

    public void update(Long id, ColumnRequest request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
        
    }

    public void delete(Long id) {
        columnRepository.deleteById(id);
    }
}
