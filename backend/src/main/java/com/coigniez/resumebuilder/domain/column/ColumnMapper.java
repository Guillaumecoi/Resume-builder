package com.coigniez.resumebuilder.domain.column;

import com.coigniez.resumebuilder.domain.column.dtos.ColumnResponse;
import com.coigniez.resumebuilder.domain.column.dtos.CreateColumnRequest;
import com.coigniez.resumebuilder.domain.column.dtos.UpdateColumnRequest;
import com.coigniez.resumebuilder.domain.columnsection.ColumnSection;
import com.coigniez.resumebuilder.domain.columnsection.ColumnSectionMapper;
import com.coigniez.resumebuilder.domain.columnsection.dtos.ColumnSectionResp;
import com.coigniez.resumebuilder.interfaces.Mapper;
import com.coigniez.resumebuilder.util.MapperUtils;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ColumnMapper implements Mapper<Column, CreateColumnRequest, UpdateColumnRequest, ColumnResponse> {

    private final ColumnSectionMapper columnSectionMapper;

    private static final Map<String, Object> DEFAULT_VALUES = Map.of(
        "columnNumber", 1,
        "paddingLeft", 10.0,
        "paddingRight", 10.0,
        "paddingTop", 20.0,
        "paddingBottom", 20.0,
        "borderLeft", 0.0,
        "borderRight", 0.0,
        "borderTop", 0.0,
        "borderBottom", 0.0
    );
    
    @Override
    public Column toEntity(@Valid CreateColumnRequest request) {
        if (request == null) {
            return null;
        }

        // Set default values
        MapperUtils.setDefaultValues(request, DEFAULT_VALUES);

        // Convert section mappings
        List<ColumnSection> sectionMappings = new ArrayList<>();
        if (request.getSectionMappings() == null) {
            request.setSectionMappings(new ArrayList<>());
        }
        request.getSectionMappings()
                .forEach(section -> sectionMappings.add(columnSectionMapper.toEntity(section)));

        return Column.builder()
                .columnNumber(request.getColumnNumber())
                .sectionMappings(sectionMappings)
                .backgroundColor(request.getBackgroundColor())
                .textColor(request.getTextColor())
                .borderColor(request.getBorderColor())
                .paddingLeft(request.getPaddingLeft())
                .paddingRight(request.getPaddingRight())
                .paddingTop(request.getPaddingTop())
                .paddingBottom(request.getPaddingBottom())
                .borderLeft(request.getBorderLeft())
                .borderRight(request.getBorderRight())
                .borderTop(request.getBorderTop())
                .borderBottom(request.getBorderBottom())
                .build();
    }

    @Override
    public ColumnResponse toDto(Column entity) {
        if (entity == null) {
            return null;
        }

        List<ColumnSectionResp> sectionMappings = new ArrayList<>();
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
    public void updateEntity(Column entity, UpdateColumnRequest request) {
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