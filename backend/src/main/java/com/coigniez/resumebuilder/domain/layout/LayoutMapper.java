package com.coigniez.resumebuilder.domain.layout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.coigniez.resumebuilder.domain.column.ColumnMapper;
import com.coigniez.resumebuilder.domain.column.dtos.ColumnResponse;
import com.coigniez.resumebuilder.domain.latex.LatexMethodMapper;
import com.coigniez.resumebuilder.domain.latex.dtos.LatexMethodResponse;
import com.coigniez.resumebuilder.domain.layout.dtos.CreateLayoutRequest;
import com.coigniez.resumebuilder.domain.layout.dtos.LayoutResponse;
import com.coigniez.resumebuilder.domain.layout.dtos.UpdateLayoutRequest;
import com.coigniez.resumebuilder.domain.layout.enums.ColorLocation;
import com.coigniez.resumebuilder.domain.layout.enums.PageSize;
import com.coigniez.resumebuilder.interfaces.Mapper;
import com.coigniez.resumebuilder.templates.ColorTemplates;
import com.coigniez.resumebuilder.templates.LayoutTemplate;
import com.coigniez.resumebuilder.util.MapperUtils;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class LayoutMapper implements Mapper<Layout, CreateLayoutRequest, UpdateLayoutRequest, LayoutResponse> {

    private final ColumnMapper columnMapper;
    private final LatexMethodMapper latexMethodMapper;

    private static final Map<String, Object> DEFAULT_VALUES = Map.of(
            "pageSize", PageSize.A4,
            "numberOfColumns", 1,
            "columnSeparator", 0.35,
            "colorScheme", ColorTemplates.EXECUTIVE_SUITE,
            "latexMethods", LayoutTemplate.getStandardMethods(),
            "sectionTitleMethod", """
                    \\newcommand{\\sectiontitle}[1] {
                        \\hspace{6pt}\\textbf{\\Large{\\uppercase{#1}}}\\\\[-4pt]
                        \\textcolor{%s}{\\rule{80pt}{2pt}}
                    }
                    """.formatted(ColorLocation.ACCENT.toString())
    );

    @Override
    public Layout toEntity(CreateLayoutRequest request) {
        // Check if the request is null
        if (request == null) {
            return null;
        }

        // Set default values
        MapperUtils.setDefaultValues(request, DEFAULT_VALUES);

        // Create the layout entity
        Layout layout = Layout.builder()
                .pageSize(request.getPageSize())
                .numberOfColumns(request.getNumberOfColumns())
                .columnSeparator(request.getColumnSeparator())
                .colorScheme(request.getColorScheme())
                .sectionTitleMethod(request.getSectionTitleMethod())
                .build();

        // Add columns and latex methods to the layout
        if (request.getColumns() != null) {
            request.getColumns().stream()
                    .map(columnMapper::toEntity)
                    .forEach(layout::addColumn);
        }

        if (request.getLatexMethods() != null) {
            request.getLatexMethods().stream()
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
    public void updateEntity(Layout entity, UpdateLayoutRequest request) {
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