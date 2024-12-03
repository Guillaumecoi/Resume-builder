package com.coigniez.resumebuilder.model.layout.column;

import org.springframework.stereotype.Service;

import com.coigniez.resumebuilder.interfaces.CrudService;
import com.coigniez.resumebuilder.model.layout.Layout;
import com.coigniez.resumebuilder.model.layout.LayoutRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Transactional
@Service
public class ColumnService implements CrudService<ColumnDTO, ColumnDTO> {

    private final ColumnRepository columnRepository;
    private final LayoutRepository layoutRepository;
    private final ColumnMapper columnMapper;
    

    public Long create(Long parentId, ColumnDTO request) {
        Column column = columnMapper.toEntity(request);
        Layout layout = layoutRepository.findById(parentId).orElseThrow(() -> new EntityNotFoundException("Layout not found"));
        layout.addColumn(column);
        
        return columnRepository.save(column).getId();
    }

    public ColumnDTO get(Long id) {
        Column column = columnRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Column not found"));
        return columnMapper.toDto(column);
    }

    public void update(Long id, ColumnDTO request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
        
    }

    public void delete(Long id) {
        columnRepository.deleteById(id);
    }
}
