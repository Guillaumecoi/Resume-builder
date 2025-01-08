package com.coigniez.resumebuilder.domain.columnholder;

import java.util.Optional;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coigniez.resumebuilder.domain.column.Column;
import com.coigniez.resumebuilder.domain.column.ColumnMapper;
import com.coigniez.resumebuilder.domain.column.dtos.ColumnResp;
import com.coigniez.resumebuilder.domain.columnholder.dtos.ColumnHolderCreateReq;
import com.coigniez.resumebuilder.domain.columnholder.dtos.ColumnHolderResp;
import com.coigniez.resumebuilder.domain.columnholder.dtos.ColumnHolderUpdateReq;
import com.coigniez.resumebuilder.domain.columnholder.headerfooter.Header;
import com.coigniez.resumebuilder.domain.columnholder.headerfooter.dtos.HeaderCreateReq;
import com.coigniez.resumebuilder.domain.columnholder.headerfooter.dtos.HeaderResp;
import com.coigniez.resumebuilder.domain.columnholder.headerfooter.dtos.HeaderUpdateReq;
import com.coigniez.resumebuilder.domain.columnholder.page.LayoutPage;
import com.coigniez.resumebuilder.domain.columnholder.page.dtos.PageCreateReq;
import com.coigniez.resumebuilder.domain.columnholder.page.dtos.PageResp;
import com.coigniez.resumebuilder.domain.columnholder.page.dtos.PageUpdateReq;
import com.coigniez.resumebuilder.interfaces.Mapper;

import jakarta.validation.Valid;

@Service
public class ColumnHolderMapper
        implements Mapper<ColumnHolder, ColumnHolderCreateReq, ColumnHolderUpdateReq, ColumnHolderResp> {

    @Autowired
    private ColumnMapper columnMapper;

    @Override
    public ColumnHolder toEntity(@Valid ColumnHolderCreateReq request) {
        if (request == null) {
            return null;
        }

        List<Column> columns = Optional.ofNullable(request.getColumns())
                .map(cols -> cols.stream().map(columnMapper::toEntity).toList())
                .orElse(List.of());

        if (request instanceof HeaderCreateReq) {
            return Header.builder()
                    .height(((HeaderCreateReq) request).getHeight())
                    .repeatOnEveryPage(((HeaderCreateReq) request).getRepeatOnEveryPage())
                    .columns(columns)
                    .build();
        } else if (request instanceof PageCreateReq) {
            return LayoutPage.builder()
                    .pageNumber(((PageCreateReq) request).getPageNumber())
                    .columns(columns)
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

        List<ColumnResp> columns = Optional.ofNullable(entity.getColumns())
                .map(cols -> cols.stream().map(columnMapper::toDto).toList())
                .orElse(List.of());

        if (entity instanceof Header) {
            return HeaderResp.builder()
                    .id(entity.getId())
                    .height(((Header) entity).getHeight())
                    .repeatOnEveryPage(((Header) entity).getRepeatOnEveryPage())
                    .columns(columns)
                    .build();
        } else if (entity instanceof LayoutPage) {
            return PageResp.builder()
                    .id(entity.getId())
                    .pageNumber(((LayoutPage) entity).getPageNumber())
                    .columns(columns)
                    .build();
        } else {
            throw new IllegalArgumentException("Unknown entity type: " + entity.getClass().getName());
        }
    }

    @Override
    public void updateEntity(ColumnHolder entity, @Valid ColumnHolderUpdateReq request) {
        if (entity == null || request == null) {
            return;
        }

        if (entity instanceof Header && request instanceof HeaderUpdateReq) {
            Header headerFooter = (Header) entity;
            HeaderUpdateReq headerFooterUpdateReq = (HeaderUpdateReq) request;

            Optional.ofNullable(headerFooterUpdateReq.getHeight()).ifPresent(headerFooter::setHeight);
            Optional.ofNullable(headerFooterUpdateReq.getRepeatOnEveryPage())
                    .ifPresent(headerFooter::setRepeatOnEveryPage);

        } else if (entity instanceof LayoutPage && request instanceof PageUpdateReq) {
            LayoutPage layoutPage = (LayoutPage) entity;
            PageUpdateReq pageUpdateReq = (PageUpdateReq) request;

            Optional.ofNullable(pageUpdateReq.getPageNumber()).ifPresent(layoutPage::setPageNumber);
        } else {
            throw new IllegalArgumentException(
                    "Unknown entity type: " + entity.getClass().getName() + " or request type: "
                            + request.getClass().getName());
        }
    }

}
