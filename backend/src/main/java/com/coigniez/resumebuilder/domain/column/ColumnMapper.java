package com.coigniez.resumebuilder.domain.column;

import com.coigniez.resumebuilder.domain.column.dtos.ColumnResp;
import com.coigniez.resumebuilder.domain.column.dtos.ColumnSimpleCreateReq;
import com.coigniez.resumebuilder.domain.column.dtos.ColumnUpdateReq;
import com.coigniez.resumebuilder.domain.columnsection.ColumnSectionMapper;
import com.coigniez.resumebuilder.domain.layout.enums.ColorLocation;
import com.coigniez.resumebuilder.interfaces.Mapper;
import com.coigniez.resumebuilder.util.MapperUtils;

import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ColumnMapper implements Mapper<LayoutColumn, ColumnSimpleCreateReq, ColumnUpdateReq, ColumnResp> {

    private final ColumnSectionMapper columnSectionMapper;

    private static final Map<String, Object> DEFAULT_VALUES = Map.ofEntries(
            Map.entry("columnSize", 1.0f),
            Map.entry("backgroundColor", ColorLocation.LIGHT_BG),
            Map.entry("textColor", ColorLocation.DARK_TEXT),
            Map.entry("borderColor", ColorLocation.ACCENT),
            Map.entry("paddingLeft", 10.0f),
            Map.entry("paddingRight", 10.0f),
            Map.entry("paddingTop", 20.0f),
            Map.entry("paddingBottom", 20.0f),
            Map.entry("borderLeft", 0.0f),
            Map.entry("borderRight", 0.0f),
            Map.entry("borderTop", 0.0f),
            Map.entry("borderBottom", 0.0f));

    @Override
    public LayoutColumn toEntity(ColumnSimpleCreateReq request) {
        if (request == null) {
            return null;
        }

        // Set the default values
        MapperUtils.setDefaultValues(request, DEFAULT_VALUES);

        LayoutColumn layoutColumn = LayoutColumn.builder()
                .columnNumber(request.getColumnNumber())
                .ColumnSize(request.getColumnSize())
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
                .backgroundImage(request.getBackgroundImage())
                .sectionMappings(new ArrayList<>())
                .build();

        // Set the child entities
        Optional.ofNullable(request.getSectionMappings()).ifPresent(
                sections -> sections
                        .forEach(section -> layoutColumn.addSectionMapping(columnSectionMapper.toEntity(section))));

        return layoutColumn;
    }

    @Override
    public ColumnResp toDto(LayoutColumn entity) {
        if (entity == null) {
            return null;
        }

        return ColumnResp.builder()
                .id(entity.getId())
                .columnNumber(entity.getColumnNumber())
                .columnSize(entity.getColumnSize())
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
                .backgroundImage(entity.getBackgroundImage())
                .sectionMappings(Optional.ofNullable(entity.getSectionMappings()).orElse(new ArrayList<>())
                        .stream().map(columnSectionMapper::toDto).toList())
                .build();
    }

    @Override
    public void updateEntity(LayoutColumn entity, ColumnUpdateReq request) {
        if (request == null) {
            return;
        }

        entity.setColumnNumber(request.getColumnNumber());
        entity.setColumnSize(request.getColumnSize());
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