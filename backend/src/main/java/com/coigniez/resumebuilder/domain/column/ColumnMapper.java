package com.coigniez.resumebuilder.domain.column;

import com.coigniez.resumebuilder.domain.columnsection.ColumnSection;
import com.coigniez.resumebuilder.domain.columnsection.ColumnSectionMapper;
import com.coigniez.resumebuilder.domain.columnsection.ColumnSectionResponse;
import com.coigniez.resumebuilder.interfaces.Mapper;

import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ColumnMapper implements Mapper<Column, ColumnRequest, ColumnResponse> {

    private final ColumnSectionMapper columnSectionMapper;
    
    @Override
    public Column toEntity(ColumnRequest dto) {
        if (dto == null) {
            return null;
        }

        List<ColumnSection> sectionMappings = new ArrayList<>();
        if (dto.getSectionMappings() != null) {
            dto.getSectionMappings().forEach(section -> sectionMappings.add(columnSectionMapper.toEntity(section)));
        }

        return Column.builder()
                .id(dto.getId())
                .columnNumber(dto.getColumnNumber())
                .sectionMappings(sectionMappings)
                .backgroundColor(dto.getBackgroundColor())
                .textColor(dto.getTextColor())
                .borderColor(dto.getBorderColor())
                .paddingLeft(dto.getPaddingLeft())
                .paddingRight(dto.getPaddingRight())
                .paddingTop(dto.getPaddingTop())
                .paddingBottom(dto.getPaddingBottom())
                .borderLeft(dto.getBorderLeft())
                .borderRight(dto.getBorderRight())
                .borderTop(dto.getBorderTop())
                .borderBottom(dto.getBorderBottom())
                .build();
    }

    @Override
    public ColumnResponse toDto(Column entity) {
        if (entity == null) {
            return null;
        }

        List<ColumnSectionResponse> sectionMappings = new ArrayList<>();
        if (entity.getSectionMappings() != null) {
            entity.getSectionMappings().forEach(section -> sectionMappings.add(columnSectionMapper.toDto(section)));
        }

        return ColumnResponse.builder()
                .id(entity.getId())
                .columnNumber(entity.getColumnNumber())
                .sectionMappings(sectionMappings)
                .backgroundColor(entity.getBackgroundColor())
                .textColor(entity.getTextColor())
                .borderColor(entity.getBorderColor())
                .paddingLeft(entity.getPaddingLeft())
                .paddingRight(entity.getPaddingRight())
                .paddingTop(entity.getPaddingTop())
                .paddingBottom(entity.getPaddingBottom())
                .borderLeft(entity.getBorderLeft())
                .borderRight(entity.getBorderRight())
                .borderTop(entity.getBorderTop())
                .borderBottom(entity.getBorderBottom())
                .build();
    }

    @Override
    public void updateEntity(Column entity, ColumnRequest request) {
        if (request == null) {
            return;
        }

        entity.setColumnNumber(request.getColumnNumber());
        entity.setBackgroundColor(request.getBackgroundColor());
        entity.setTextColor(request.getTextColor());
        entity.setBorderColor(request.getBorderColor());
        entity.setPaddingLeft(request.getPaddingLeft());
        entity.setPaddingRight(request.getPaddingRight());
        entity.setPaddingTop(request.getPaddingTop());
        entity.setPaddingBottom(request.getPaddingBottom());
        entity.setBorderLeft(request.getBorderLeft());
        entity.setBorderRight(request.getBorderRight());
        entity.setBorderTop(request.getBorderTop());
        entity.setBorderBottom(request.getBorderBottom());
    }
}