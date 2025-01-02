package com.coigniez.resumebuilder.domain.layoutsubsection;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.coigniez.resumebuilder.domain.layoutsectionItem.LayoutSectionItemMapper;
import com.coigniez.resumebuilder.domain.layoutsubsection.dtos.LayoutSubSectionResp;
import com.coigniez.resumebuilder.domain.layoutsubsection.dtos.LayoutSubSectionSimpleCreateReq;
import com.coigniez.resumebuilder.domain.layoutsubsection.dtos.LayoutSubSectionUpdateReq;
import com.coigniez.resumebuilder.interfaces.Mapper;
import com.coigniez.resumebuilder.util.MapperUtils;

import jakarta.validation.Valid;

public class LayoutSubSectionMapper implements
        Mapper<LayoutSubSection, LayoutSubSectionSimpleCreateReq, LayoutSubSectionUpdateReq, LayoutSubSectionResp> {

    private static final Map<String, Object> DEFAULT_VALUES = Map.of(
        "hidden", false,
        "defaultOrder", true
    );

    @Autowired
    private LayoutSectionItemMapper layoutSectionItemMapper;

    @Override
    public LayoutSubSection toEntity(@Valid LayoutSubSectionSimpleCreateReq request) {
        if (request == null) {
            return null;
        }

        MapperUtils.setDefaultValues(request, DEFAULT_VALUES);

        return LayoutSubSection.builder()
            .sectionOrder(request.getSectionOrder())
            .alignment(request.getAlignment())
            .hidden(request.getHidden())
            .defaultOrder(request.getDefaultOrder())
            .build();
    }

    @Override
    public LayoutSubSectionResp toDto(LayoutSubSection entity) {
        if (entity == null) {
            return null;
        }

        return LayoutSubSectionResp.builder()
            .id(entity.getId())
            .sectionOrder(entity.getSectionOrder())
            .alignment(entity.getAlignment())
            .hidden(entity.getHidden())
            .defaultOrder(entity.getDefaultOrder())
            .items(entity.getLayoutSectionItems().stream().map(layoutSectionItemMapper::toDto).toList())
            .build();
    }

    @Override
    public void updateEntity(LayoutSubSection entity, @Valid LayoutSubSectionUpdateReq request) {
        if (request == null) {
            return;
        }

        entity.setSectionOrder(request.getSectionOrder());
        entity.setAlignment(request.getAlignment());
        entity.setHidden(request.getHidden());
        entity.setDefaultOrder(request.getDefaultOrder());
    }

}
