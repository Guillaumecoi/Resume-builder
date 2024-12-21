package com.coigniez.resumebuilder.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.coigniez.resumebuilder.domain.column.Column;
import com.coigniez.resumebuilder.domain.column.ColumnMapper;
import com.coigniez.resumebuilder.domain.column.ColumnRepository;
import com.coigniez.resumebuilder.domain.column.dtos.ColumnResponse;
import com.coigniez.resumebuilder.domain.column.dtos.CreateColumnRequest;
import com.coigniez.resumebuilder.domain.column.dtos.UpdateColumnRequest;
import com.coigniez.resumebuilder.domain.columnsection.ColumnSectionRepository;
import com.coigniez.resumebuilder.domain.columnsection.dtos.CreateColumnSectionRequest;
import com.coigniez.resumebuilder.domain.columnsection.dtos.UpdateColumnSectionRequest;
import com.coigniez.resumebuilder.domain.layout.LayoutRepository;
import com.coigniez.resumebuilder.interfaces.ParentEntityService;
import com.coigniez.resumebuilder.util.SecurityUtils;

import jakarta.persistence.EntityNotFoundException;
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
        hasAccessLayout(request.getLayoutId());

        // Create the column entity
        Column column = columnMapper.toEntity(request);
        layoutRepository.findById(request.getLayoutId())
                .orElseThrow(() -> new EntityNotFoundException("Layout not found"))
                .addColumn(column);

        // Save the column entity
        return columnRepository.save(column).getId();
    }

    @Override
    public ColumnResponse get(Long id) {
        // Check if the current user has access to the column
        hasAccess(id);

        // Get the column entity
        return columnRepository.findById(id)
                .map(columnMapper::toDto)
                .orElseThrow(() -> new EntityNotFoundException("Column not found"));
    }

    @Override
    public void update(UpdateColumnRequest request) {
        // Check if the current user has access to the column
        hasAccess(request.getId());

        for (CreateColumnSectionRequest section : request.getCreateSectionMappings()) {
            section.setColumnId(request.getId());
            columnSectionService.create(section);
        }
        for (UpdateColumnSectionRequest section : request.getUpdateSectionMappings()) {
            // Check if the section belongs to the column
            if (section.getId() == null) {
                throw new IllegalArgumentException("Section id is required");
            }
            if (columnSectionRepository.findById(section.getId())
                    .orElseThrow(() -> new EntityNotFoundException("ColumnSection not found"))
                    .getColumn().getId() != request.getId()) {
                throw new IllegalArgumentException("Section does not belong to the column");
            }

            columnSectionService.update(section);
        }

        // Update the entity
        Column column = columnRepository.findById(request.getId())
                .orElseThrow(() -> new EntityNotFoundException("Column not found"));
        columnMapper.updateEntity(column, request);

        // Save the updated entity
        columnRepository.save(column);

    }

    @Override
    public void delete(Long id) {
        // Check if the current user has access to the column
        hasAccess(id);

        // Remove the column from the layout
        Column column = columnRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Column not found"));
        Optional.ofNullable(column.getLayout()).ifPresent(layout -> layout.removeColumn(column));

        // Delete the column from the database
        columnRepository.deleteById(id);
    }

    @Override
    public List<ColumnResponse> getAllByParentId(Long layoutId) {
        // Check if the current user has access to the layout
        hasAccessLayout(layoutId);

        // Get all columns from the layout
        return columnRepository.findAllByLayoutId(layoutId).stream()
                .map(columnMapper::toDto)
                .toList();
    }

    @Override
    public void removeAllByParentId(Long layoutId) {
        // Check if the current user has access to the layout
        hasAccessLayout(layoutId);

        // Remove all columns from the layout
        layoutRepository.findById(layoutId)
                .orElseThrow(() -> new EntityNotFoundException("Layout not found"))
                .clearColumns();

        // Delete all columns from the database
        columnRepository.deleteAllByLayoutId(layoutId);
    }

    /**
     * Check if the current user has access to the column
     * 
     * @param id The column id
     */
    private void hasAccess(long id) {
        String owner = columnRepository.findCreatedBy(id)
                .orElseThrow(() -> new EntityNotFoundException("Column not found"));
        securityUtils.hasAccess(List.of(owner));
    }

    /**
     * Check if the current user has access to the layout
     * 
     * @param id The layout id
     */
    private void hasAccessLayout(long id) {
        String owner = layoutRepository.findCreatedBy(id)
                .orElseThrow(() -> new EntityNotFoundException("Layout not found"));
        securityUtils.hasAccess(List.of(owner));
    }
}
