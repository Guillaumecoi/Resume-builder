package com.coigniez.resumebuilder.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.coigniez.resumebuilder.domain.column.LayoutColumn;
import com.coigniez.resumebuilder.domain.column.ColumnMapper;
import com.coigniez.resumebuilder.domain.column.dtos.ColumnResp;
import com.coigniez.resumebuilder.domain.column.dtos.ColumnCreateReq;
import com.coigniez.resumebuilder.domain.column.dtos.ColumnUpdateReq;
import com.coigniez.resumebuilder.domain.column.enums.BackgroungImage;
import com.coigniez.resumebuilder.domain.columnholder.ColumnHolder;
import com.coigniez.resumebuilder.file.FileStorageService;
import com.coigniez.resumebuilder.interfaces.ParentEntityService;
import com.coigniez.resumebuilder.repository.ColumnHolderRepository;
import com.coigniez.resumebuilder.repository.ColumnRepository;
import com.coigniez.resumebuilder.util.ExceptionUtils;
import com.coigniez.resumebuilder.util.ParentRepositoryUtil;
import com.coigniez.resumebuilder.util.SecurityUtils;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ColumnService
        implements ParentEntityService<ColumnCreateReq, ColumnUpdateReq, ColumnResp, Long> {

    private final ColumnRepository columnRepository;
    private final ColumnHolderRepository columnHolderRepository;
    private final ColumnMapper columnMapper;
    private final SecurityUtils securityUtils;
    private final FileStorageService fileStorageService;
    private final ParentRepositoryUtil parentRepositoryUtil;

    @Override
    public Long create(ColumnCreateReq request) {
        // Check if the current user has access to the columnHolder
        securityUtils.hasAccessColumnHolder(request.getColumnHolderId());

        // Create the entity
        LayoutColumn column = columnMapper.toEntity(request);

        // Add the column to the columnHolder
        columnHolderRepository.findById(request.getColumnHolderId())
                .orElseThrow(() -> ExceptionUtils.entityNotFound("ColumnHolder", request.getColumnHolderId()))
                .addColumn(column);

        // Save the entity
        return columnRepository.save(column).getId();
    }

    public Long CreateWithPicture(MultipartFile file, ColumnCreateReq request) {
        // Check if the current user has access to the columnHolder
        securityUtils.hasAccessColumnHolder(request.getColumnHolderId());

        // Save the file to the file storage and add the path to the request
        String path = fileStorageService.saveFile(file, securityUtils.getUserName());
        BackgroungImage backgroungImage = Optional.ofNullable(request.getBackgroundImage())
                .orElse(new BackgroungImage());
        backgroungImage.setImagePath(path);
        request.setBackgroundImage(backgroungImage);

        // Save the entity
        return create(request);
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
        LayoutColumn column = columnRepository.findById(request.getId())
                .orElseThrow(() -> ExceptionUtils.entityNotFound("Column", request.getId()));
        columnMapper.updateEntity(column, request);

        // Save the updated entity
        columnRepository.save(column);

    }

    @Override
    public void delete(Long id) {
        // Check if the current user has access to the column
        securityUtils.hasAccessColumn(id);

        // Get the Column Holder
        LayoutColumn column = columnRepository.findById(id)
                .orElseThrow(() -> ExceptionUtils.entityNotFound("Column", id));

        // Remove the column from the column holder
        ColumnHolder columnHolder = column.getColumnHolder();
        columnHolder.removeColumn(column);

        // Delete the column from the database
        columnRepository.deleteById(id);
    }

    @Override
    public List<ColumnResp> getAllByParentId(Long columnHolderId) {
        // Check if the current user has access to the layout
        securityUtils.hasAccessLayout(columnHolderId);

        // Get all columns from the database
        return parentRepositoryUtil
                .findAllByParentId(LayoutColumn.class, ColumnHolder.class, columnHolderId, "columnNumber")
                .stream().map(columnMapper::toDto).toList();
    }

    @Override
    public void removeAllByParentId(Long columnHolderId) {
        // Check if the current user has access to the layout
        securityUtils.hasAccessLayout(columnHolderId);

        ColumnHolder columnHolder = columnHolderRepository.findById(columnHolderId)
                .orElseThrow(() -> ExceptionUtils.entityNotFound("ColumnHolder", columnHolderId));
        columnHolder.clearColumns();

        // Delete all columns from the database
        columnHolderRepository.save(columnHolder);

    }

}
