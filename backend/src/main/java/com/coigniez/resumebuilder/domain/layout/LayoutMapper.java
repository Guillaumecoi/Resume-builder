package com.coigniez.resumebuilder.domain.layout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.coigniez.resumebuilder.domain.column.ColumnMapper;
import com.coigniez.resumebuilder.domain.column.ColumnResponse;
import com.coigniez.resumebuilder.domain.latex.LatexMethodMapper;
import com.coigniez.resumebuilder.domain.latex.LatexMethodResponse;
import com.coigniez.resumebuilder.interfaces.Mapper;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class LayoutMapper implements Mapper<Layout, LayoutRequest, LayoutResponse> {

    private final ColumnMapper columnMapper;
    private final LatexMethodMapper latexMethodMapper;

    @Override
    public Layout toEntity(LayoutRequest dto) {
        // Check if the request is null
        if (dto == null) {
            return null;
        }

        // Create the layout entity
        Layout layout = Layout.builder()
                .pageSize(dto.getPageSize())
                .numberOfColumns(dto.getNumberOfColumns())
                .columnSeparator(dto.getColumnSeparator())
                .colorScheme(dto.getColorScheme())
                .sectionTitleMethod(dto.getSectionTitleMethod())
                .build();

        // Add columns and latex methods to the layout
        if (dto.getColumns() != null) {
            dto.getColumns().stream()
                    .map(columnMapper::toEntity)
                    .forEach(layout::addColumn);
        }

        if (dto.getLatexMethods() != null) {
            dto.getLatexMethods().stream()
                    .map(latexMethodMapper::toEntity)
                    .forEach(layout::addLatexMethod);
        }

        return layout;
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

        Set<LatexMethodResponse> latexMethodDTOs = new HashSet<>();
        if (entity.getLatexMethods() != null) {
            entity.getLatexMethods().forEach(method -> latexMethodDTOs.add(latexMethodMapper.toDto(method)));
        }

        return LayoutResponse.builder()
                .id(entity.getId())
                .pageSize(entity.getPageSize())
                .columns(columnDTOs)
                .numberOfColumns(entity.getNumberOfColumns())
                .columnSeparator(entity.getColumnSeparator())
                .colorScheme(entity.getColorScheme())
                .latexMethods(latexMethodDTOs)
                .sectionTitleMethod(entity.getSectionTitleMethod())
                .build();
    }

    @Override
    public void updateEntity(Layout entity, LayoutRequest request) {
        if (entity == null || request == null) {
            return;
        }

        entity.setPageSize(request.getPageSize());
        entity.setNumberOfColumns(request.getNumberOfColumns());
        entity.setColumnSeparator(request.getColumnSeparator());
        entity.setColorScheme(request.getColorScheme());
        entity.setSectionTitleMethod(request.getSectionTitleMethod());
    }
}