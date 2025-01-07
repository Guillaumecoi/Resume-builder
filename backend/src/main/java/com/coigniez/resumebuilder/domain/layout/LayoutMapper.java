package com.coigniez.resumebuilder.domain.layout;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.coigniez.resumebuilder.domain.columnholder.ColumnHolderMapper;
import com.coigniez.resumebuilder.domain.columnholder.headerfooter.HeaderFooter;
import com.coigniez.resumebuilder.domain.columnholder.page.LayoutPage;
import com.coigniez.resumebuilder.domain.latex.LatexMethodMapper;
import com.coigniez.resumebuilder.domain.latex.dtos.LatexMethodResp;
import com.coigniez.resumebuilder.domain.layout.dtos.LayoutCreateReq;
import com.coigniez.resumebuilder.domain.layout.dtos.LayoutResp;
import com.coigniez.resumebuilder.domain.layout.dtos.LayoutUpdateReq;
import com.coigniez.resumebuilder.domain.layout.enums.PageSize;
import com.coigniez.resumebuilder.interfaces.Mapper;
import com.coigniez.resumebuilder.templates.color.ColorTemplates;
import com.coigniez.resumebuilder.templates.methods.LatexMethodTemplates;
import com.coigniez.resumebuilder.util.MapperUtils;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class LayoutMapper implements Mapper<Layout, LayoutCreateReq, LayoutUpdateReq, LayoutResp> {

    private final LatexMethodMapper latexMethodMapper;
    private final ColumnHolderMapper columnHolderMapper;

    private static final Map<String, Object> DEFAULT_VALUES = Map.of(
            "pageSize", PageSize.A4,
            "colorScheme", ColorTemplates.EXECUTIVE_SUITE,
            "latexMethods", LatexMethodTemplates.getStandardMethods());

    @Override
    public Layout toEntity(LayoutCreateReq request) {
        // Check if the request is null
        if (request == null) {
            return null;
        }

        // Set default values
        MapperUtils.setDefaultValues(request, DEFAULT_VALUES);

        // Create the layout entity
        Layout layout = Layout.builder()
                .pageSize(request.getPageSize())
                .colorScheme(request.getColorScheme())
                .latexMethods(new ArrayList<>())
                .build();

        // Set the child entities
        request.getLatexMethods().forEach(method -> layout.addLatexMethod(latexMethodMapper.toEntity(method)));
        if (request.getHeader() != null) {
            HeaderFooter header = (HeaderFooter) columnHolderMapper.toEntity(request.getHeader());
            header.setLayout(layout);
            layout.setHeader(header);
        }
        if (request.getFooter() != null) {
            HeaderFooter footer = (HeaderFooter) columnHolderMapper.toEntity(request.getFooter());
            footer.setLayout(layout);
            layout.setFooter(footer);
        }
        Optional.ofNullable(request.getPages()).ifPresent(
                pages -> pages.forEach(page -> layout.addPage((LayoutPage) columnHolderMapper.toEntity(page))));

        return layout;
    }

    @Override
    public LayoutResp toDto(Layout entity) {
        if (entity == null) {
            return null;
        }

        List<LatexMethodResp> latexMethodDTOs = new ArrayList<>();
        if (entity.getLatexMethods() != null) {
            entity.getLatexMethods().forEach(method -> latexMethodDTOs.add(latexMethodMapper.toDto(method)));
        }

        return LayoutResp.builder()
                .id(entity.getId())
                .pageSize(entity.getPageSize())
                .colorScheme(entity.getColorScheme())
                .latexMethods(latexMethodDTOs)
                .build();
    }

    @Override
    public void updateEntity(Layout entity, LayoutUpdateReq request) {
        if (entity == null || request == null) {
            return;
        }

        entity.setPageSize(request.getPageSize());
        entity.setColorScheme(request.getColorScheme());
    }
}