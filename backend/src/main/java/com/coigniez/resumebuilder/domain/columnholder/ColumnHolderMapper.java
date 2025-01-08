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
import com.coigniez.resumebuilder.domain.columnholder.headerfooter.HeaderFooter;
import com.coigniez.resumebuilder.domain.columnholder.headerfooter.dtos.HeaderFooterCreateReq;
import com.coigniez.resumebuilder.domain.columnholder.headerfooter.dtos.HeaderFooterResp;
import com.coigniez.resumebuilder.domain.columnholder.headerfooter.dtos.HeaderFooterUpdateReq;
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

        if (request instanceof HeaderFooterCreateReq) {
            return HeaderFooter.builder()
                    .height(((HeaderFooterCreateReq) request).getHeight())
                    .repeatOnEveryPage(((HeaderFooterCreateReq) request).getRepeatOnEveryPage())
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

        if (entity instanceof HeaderFooter) {
            return HeaderFooterResp.builder()
                    .height(((HeaderFooter) entity).getHeight())
                    .repeatOnEveryPage(((HeaderFooter) entity).getRepeatOnEveryPage())
                    .columns(columns)
                    .build();
        } else if (entity instanceof LayoutPage) {
            return PageResp.builder()
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

        if (entity instanceof HeaderFooter && request instanceof HeaderFooterUpdateReq) {
            HeaderFooter headerFooter = (HeaderFooter) entity;
            HeaderFooterUpdateReq headerFooterUpdateReq = (HeaderFooterUpdateReq) request;

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
