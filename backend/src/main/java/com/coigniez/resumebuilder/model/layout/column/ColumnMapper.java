package com.coigniez.resumebuilder.model.layout.column;

import com.coigniez.resumebuilder.interfaces.Mapper;
import com.coigniez.resumebuilder.model.layout.column.ColumnSection.ColumnSection;
import com.coigniez.resumebuilder.model.layout.column.ColumnSection.ColumnSectionDTO;
import com.coigniez.resumebuilder.model.layout.column.ColumnSection.ColumnSectionMapper;

import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ColumnMapper implements Mapper<Column, ColumnDTO, ColumnDTO> {

    private final ColumnSectionMapper columnSectionMapper;
    
    @Override
    public Column toEntity(ColumnDTO dto) {
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
    public ColumnDTO toDto(Column entity) {
        if (entity == null) {
            return null;
        }

        List<ColumnSectionDTO> sectionMappings = new ArrayList<>();
        if (entity.getSectionMappings() != null) {
            entity.getSectionMappings().forEach(section -> sectionMappings.add(columnSectionMapper.toDto(section)));
        }

        return ColumnDTO.builder()
                .id(entity.getId())
                .columnNumber(entity.getColumnNumber())
                .sectionMappings(sectionMappings)
                .backgroundColor(entity.getBackgroundColor())
                .textColor(entity.getTextColor())
                .paddingLeft(entity.getPaddingLeft())
                .paddingRight(entity.getPaddingRight())
                .paddingTop(entity.getPaddingTop())
                .paddingBottom(entity.getPaddingBottom())
                .layoutId(entity.getLayout().getId())
                .build();
    }
}