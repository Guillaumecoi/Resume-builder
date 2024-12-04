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
                .paddingLeft(dto.getPaddingLeft())
                .paddingRight(dto.getPaddingRight())
                .paddingTop(dto.getPaddingTop())
                .paddingBottom(dto.getPaddingBottom())
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
                .paddingLeft(entity.getPaddingLeft())
                .paddingRight(entity.getPaddingRight())
                .paddingTop(entity.getPaddingTop())
                .paddingBottom(entity.getPaddingBottom())
                .build();
    }
}