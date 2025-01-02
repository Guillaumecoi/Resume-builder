package com.coigniez.resumebuilder.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.coigniez.resumebuilder.domain.column.Column;
import com.coigniez.resumebuilder.domain.column.ColumnMapper;
import com.coigniez.resumebuilder.domain.column.dtos.ColumnResponse;
import com.coigniez.resumebuilder.domain.column.dtos.CreateColumnRequest;
import com.coigniez.resumebuilder.domain.column.dtos.UpdateColumnRequest;
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
        implements ParentEntityService<CreateColumnRequest, UpdateColumnRequest, ColumnResponse, Long> {

    private final ColumnRepository columnRepository;
    private final LayoutRepository layoutRepository;
    private final ColumnSectionRepository columnSectionRepository;
    private final ColumnSectionService columnSectionService;
    private final ColumnMapper columnMapper;
    private final SecurityUtils securityUtils;

    @Override
    public Long create(CreateColumnRequest request) {
        // Check if the current user has access to the layout
        securityUtils.hasAccessLayout(request.getLayoutId());

        // Create the column entity
        Column column = columnMapper.toEntity(request);
        layoutRepository.findById(request.getLayoutId())
                .orElseThrow(() -> ExceptionUtils.entityNotFound("Layout", request.getLayoutId()))
                .addColumn(column);

        // Save the column entity
        return columnRepository.save(column).getId();
    }

    @Override
    public ColumnResponse get(Long id) {
        // Check if the current user has access to the column
        securityUtils.hasAccessColumn(id);

        // Get the column entity
        return columnRepository.findById(id)
                .map(columnMapper::toDto)
                .orElseThrow(() -> ExceptionUtils.entityNotFound("Column", id));
    }

    @Override
    public void update(UpdateColumnRequest request) {
        // Check if the current user has access to the column
        securityUtils.hasAccessColumn(request.getId());

        for (ColumnSectionCreateReq section : request.getCreateSectionMappings()) {
            section.setColumnId(request.getId());
            columnSectionService.create(section);
        }
        for (ColumnSectionUpdateReq section : request.getUpdateSectionMappings()) {
            // Check if the section belongs to the column
            if (section.getId() == null) {
                throw new IllegalArgumentException("Section id is required");
            }
            if (columnSectionRepository.findById(section.getId())
                    .orElseThrow(() -> ExceptionUtils.entityNotFound("ColumnSection", section.getId()))
                    .getColumn().getId() != request.getId()) {
                throw new IllegalArgumentException("Section does not belong to the column");
            }

            columnSectionService.update(section);
        }

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

        // Remove the column from the layout
        Column column = columnRepository.findById(id)
                .orElseThrow(() -> ExceptionUtils.entityNotFound("Column", id));
        Optional.ofNullable(column.getLayout()).ifPresent(layout -> layout.removeColumn(column));

        // Delete the column from the database
        columnRepository.deleteById(id);
    }

    @Override
    public List<ColumnResponse> getAllByParentId(Long layoutId) {
        // Check if the current user has access to the layout
        securityUtils.hasAccessLayout(layoutId);

        // Get all columns from the layout
        return columnRepository.findAllByLayoutId(layoutId).stream()
                .map(columnMapper::toDto)
                .toList();
    }

    @Override
    public void removeAllByParentId(Long layoutId) {
        // Check if the current user has access to the layout
        securityUtils.hasAccessLayout(layoutId);

        // Remove all columns from the layout
        layoutRepository.findById(layoutId)
                .orElseThrow(() -> ExceptionUtils.entityNotFound("Layout", layoutId))
                .clearColumns();

        // Delete all columns from the database
        columnRepository.deleteAllByLayoutId(layoutId);
    }

}
