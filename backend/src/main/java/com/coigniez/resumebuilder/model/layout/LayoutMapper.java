package com.coigniez.resumebuilder.model.layout;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.coigniez.resumebuilder.interfaces.Mapper;
import com.coigniez.resumebuilder.model.layout.column.Column;
import com.coigniez.resumebuilder.model.layout.column.ColumnResponse;
import com.coigniez.resumebuilder.model.layout.column.ColumnMapper;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class LayoutMapper implements Mapper<Layout, LayoutRequest, LayoutResponse> {

    private final ColumnMapper columnMapper;

    @Override
    public Layout toEntity(LayoutRequest dto) {
        if (dto == null) {
            return null;
        }

        List<Column> columns = new ArrayList<>();
        if (dto.getColumns() != null) {
            dto.getColumns().forEach(column -> columns.add(columnMapper.toEntity(column)));
        }

        return Layout.builder()
                .id(dto.getId())
                .pageSize(dto.getPageSize())
                .columns(columns)
                .numberOfColumns(dto.getNumberOfColumns())
                .columnSeparator(dto.getColumnSeparator())
                .colorScheme(dto.getColorScheme())
                .latexCommands(dto.getLatexCommands())
                .build();
    }

    @Override
    public LayoutResponse toDto(Layout entity) {
        if (entity == null) {
            return null;
        }

        List<ColumnResponse> columnDTOs = new ArrayList<>();
        if (entity.getColumns() != null) {
            entity.getColumns().forEach(column -> columnDTOs.add(columnMapper.toDto(column)));
        }

        return LayoutResponse.builder()
                .id(entity.getId())
                .pageSize(entity.getPageSize())
                .columns(columnDTOs)
                .numberOfColumns(entity.getNumberOfColumns())
                .columnSeparator(entity.getColumnSeparator())
                .colorScheme(entity.getColorScheme())
                .latexCommands(entity.getLatexCommands())
                .build();
    }
}