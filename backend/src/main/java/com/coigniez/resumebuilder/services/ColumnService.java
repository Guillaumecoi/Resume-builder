package com.coigniez.resumebuilder.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.coigniez.resumebuilder.domain.column.Column;
import com.coigniez.resumebuilder.domain.column.ColumnMapper;
import com.coigniez.resumebuilder.domain.column.dtos.ColumnResp;
import com.coigniez.resumebuilder.domain.column.dtos.ColumnCreateReq;
import com.coigniez.resumebuilder.domain.column.dtos.ColumnUpdateReq;
import com.coigniez.resumebuilder.domain.columnsection.dtos.ColumnSectionCreateReq;
import com.coigniez.resumebuilder.domain.columnsection.dtos.ColumnSectionUpdateReq;
import com.coigniez.resumebuilder.interfaces.ParentEntityService;
import com.coigniez.resumebuilder.repository.ColumnRepository;
import com.coigniez.resumebuilder.repository.ColumnSectionRepository;
import com.coigniez.resumebuilder.repository.LayoutRepository;
import com.coigniez.resumebuilder.util.ExceptionUtils;
import com.coigniez.resumebuilder.util.SecurityUtils;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ColumnService
        implements ParentEntityService<ColumnCreateReq, ColumnUpdateReq, ColumnResp, Long> {

    private final ColumnRepository columnRepository;
    private final LayoutRepository layoutRepository;
    private final ColumnSectionRepository columnSectionRepository;
    private final ColumnSectionService columnSectionService;
    private final ColumnMapper columnMapper;
    private final SecurityUtils securityUtils;

    @Override
    public Long create(ColumnCreateReq request) {
        //TODO: Implement the create method
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public ColumnResp get(Long id) {
        // Check if the current user has access to the column
        securityUtils.hasAccessColumn(id);

        // Get the column entity
        return columnRepository.findById(id)
                .map(columnMapper::toDto)
                .orElseThrow(() -> ExceptionUtils.entityNotFound("Column", id));
    }

    @Override
    public void update(ColumnUpdateReq request) {
        // Check if the current user has access to the column
        securityUtils.hasAccessColumn(request.getId());

        // Update the entity
        Column column = columnRepository.findById(request.getId())
                .orElseThrow(() -> ExceptionUtils.entityNotFound("Column", request.getId()));
        columnMapper.updateEntity(column, request);

        // Save the updated entity
        columnRepository.save(column);

    }

    @Override
    public void delete(Long id) {
        // Check if the current user has access to the column
        securityUtils.hasAccessColumn(id);

        //TODO: Remove the column from the layout

        // Delete the column from the database
        columnRepository.deleteById(id);
    }

    @Override
    public List<ColumnResp> getAllByParentId(Long layoutId) {
        // Check if the current user has access to the layout
        securityUtils.hasAccessLayout(layoutId);

        // Get all columns from the layout
        return null;
    }

    @Override
    public void removeAllByParentId(Long layoutId) {
        // Check if the current user has access to the layout
        securityUtils.hasAccessLayout(layoutId);

        //TODO: Remove all columns from the layout
        
        // Delete all columns from the database
        
    }

}
