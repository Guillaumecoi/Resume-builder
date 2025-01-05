package com.coigniez.resumebuilder.domain.columnholder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coigniez.resumebuilder.domain.column.ColumnMapper;
import com.coigniez.resumebuilder.domain.columnholder.dtos.ColumnHolderCreateRequest;
import com.coigniez.resumebuilder.domain.columnholder.dtos.ColumnHolderResp;
import com.coigniez.resumebuilder.domain.columnholder.dtos.ColumnHolderUpdateRequest;
import com.coigniez.resumebuilder.domain.columnholder.headerfooter.HeaderFooter;
import com.coigniez.resumebuilder.domain.columnholder.headerfooter.dtos.HeaderFooterCreateRequest;
import com.coigniez.resumebuilder.domain.columnholder.headerfooter.dtos.HeaderFooterResp;
import com.coigniez.resumebuilder.domain.columnholder.page.LayoutPage;
import com.coigniez.resumebuilder.domain.columnholder.page.dtos.PageCreateRequest;
import com.coigniez.resumebuilder.domain.columnholder.page.dtos.PageResp;
import com.coigniez.resumebuilder.interfaces.Mapper;

import jakarta.validation.Valid;

@Service
public class ColumnHolderMapper
        implements Mapper<ColumnHolder, ColumnHolderCreateRequest, ColumnHolderUpdateRequest, ColumnHolderResp> {
    
    @Autowired
    private ColumnMapper columnMapper;

    @Override
    public ColumnHolder toEntity(@Valid ColumnHolderCreateRequest request) {
        if (request == null) {
            return null;
        }

        if (request instanceof HeaderFooterCreateRequest) {
            return HeaderFooter.builder()
                    .height(((HeaderFooterCreateRequest) request).getHeight())
                    .repeatOnEveryPage(((HeaderFooterCreateRequest) request).getRepeatOnEveryPage())
                    .columns(request.getColumns().stream().map(columnMapper::toEntity).toList())
                    .build();
        } else if (request instanceof PageCreateRequest) {
            return LayoutPage.builder()
                    .pageNumber(((PageCreateRequest) request).getPageNumber())
                    .columns(request.getColumns().stream().map(columnMapper::toEntity).toList())
                    .build();
        } else {
            throw new IllegalArgumentException("Unknown request type: " + request.getClass().getName());
        }
    }

    @Override
    public ColumnHolderResp toDto(ColumnHolder entity) {
        if (entity == null) {
            return null;
        }

        if (entity instanceof HeaderFooter) {
            return HeaderFooterResp.builder()
                    .height(((HeaderFooter) entity).getHeight())
                    .repeatOnEveryPage(((HeaderFooter) entity).getRepeatOnEveryPage())
                    .columns(entity.getColumns().stream().map(columnMapper::toDto).toList())
                    .build();
        } else if (entity instanceof LayoutPage) {
            return PageResp.builder()
                    .pageNumber(((LayoutPage) entity).getPageNumber())
                    .columns(entity.getColumns().stream().map(columnMapper::toDto).toList())
                    .build();
        } else {
            throw new IllegalArgumentException("Unknown entity type: " + entity.getClass().getName());
        }
    }

    @Override
    public void updateEntity(ColumnHolder entity, @Valid ColumnHolderUpdateRequest request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateEntity'");
    }

}
