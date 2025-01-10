package com.coigniez.resumebuilder.services;

import org.springframework.stereotype.Service;

import com.coigniez.resumebuilder.domain.columnholder.ColumnHolder;
import com.coigniez.resumebuilder.domain.columnholder.ColumnHolderMapper;
import com.coigniez.resumebuilder.domain.columnholder.dtos.ColumnHolderCreateReq;
import com.coigniez.resumebuilder.domain.columnholder.dtos.ColumnHolderResp;
import com.coigniez.resumebuilder.domain.columnholder.dtos.ColumnHolderUpdateReq;
import com.coigniez.resumebuilder.domain.columnholder.header.Header;
import com.coigniez.resumebuilder.domain.columnholder.header.dtos.HeaderSimpleCreateReq;
import com.coigniez.resumebuilder.domain.columnholder.page.LayoutPage;
import com.coigniez.resumebuilder.domain.layout.Layout;
import com.coigniez.resumebuilder.interfaces.CrudService;
import com.coigniez.resumebuilder.repository.ColumnHolderRepository;
import com.coigniez.resumebuilder.repository.LayoutRepository;
import com.coigniez.resumebuilder.util.ExceptionUtils;
import com.coigniez.resumebuilder.util.SecurityUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ColumnHolderService implements CrudService<ColumnHolderCreateReq, ColumnHolderUpdateReq, ColumnHolderResp, Long> {

    private final ColumnHolderRepository columnHolderRepository;
    private final LayoutRepository layoutRepository;
    private final ColumnHolderMapper columnHolderMapper;
    private final SecurityUtils securityUtils;

    @Override
    public Long create(ColumnHolderCreateReq request) {
        // Check if the user has access to the layout
        securityUtils.hasAccessLayout(request.getLayoutId());

        // Create the columnHolder entity
        ColumnHolder columnHolder = columnHolderMapper.toEntity(request);

        // Add the columnHolder to the layout
        Layout layout = layoutRepository.findById(request.getLayoutId())
                .orElseThrow(() -> ExceptionUtils.entityNotFound("Layout", request.getLayoutId()));

        if (request instanceof HeaderSimpleCreateReq) {
            if (layout.getHeader() != null) {
                throw new IllegalArgumentException("Layout already has a header");
            }
            layout.setHeader((Header) columnHolder);
            columnHolder.setLayout(layout);
        } else {
            layout.addPage((LayoutPage) columnHolder);
        }

        // Save the columnHolder
        return columnHolderRepository.save(columnHolder).getId();
    }

    @Override
    public ColumnHolderResp get(Long id) {
        // Check if the user has access to the columnHolder
        securityUtils.hasAccessColumnHolder(id);

        // Retrieve and return the columnHolder
        return columnHolderRepository.findById(id)
                .map(columnHolderMapper::toDto)
                .orElseThrow(() -> ExceptionUtils.entityNotFound("ColumnHolder", id));
    }

    @Override
    public void update(ColumnHolderUpdateReq request) {
        // Check if the user has access to the columnHolder
        securityUtils.hasAccessColumnHolder(request.getId());

        // Get and update the existing entity
        ColumnHolder columnHolder = columnHolderRepository.findById(request.getId())
                .orElseThrow(() -> ExceptionUtils.entityNotFound("ColumnHolder", request.getId()));
        columnHolderMapper.updateEntity(columnHolder, request);

        // Save the updated entity
        columnHolderRepository.save(columnHolder);
    }

    @Override
    public void delete(Long id) {
        // Check if the user has access to the columnHolder
        securityUtils.hasAccessColumnHolder(id);

        // Get the columnHolder and its layout
        ColumnHolder columnHolder = columnHolderRepository.findById(id)
                .orElseThrow(() -> ExceptionUtils.entityNotFound("ColumnHolder", id));
        Layout layout = columnHolder.getLayout();

        // Remove the columnHolder from the layout
        if (columnHolder instanceof Header) {
            layout.setHeader(null);
            columnHolder.setLayout(null);
        } else {
            layout.removePage((LayoutPage) columnHolder);
        }

        // Save the layout
        layoutRepository.save(layout);
    }
}