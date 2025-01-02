package com.coigniez.resumebuilder.domain.subsection;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coigniez.resumebuilder.domain.sectionitem.SectionItemMapper;
import com.coigniez.resumebuilder.domain.subsection.dtos.SubSectionResp;
import com.coigniez.resumebuilder.domain.subsection.dtos.SubSectionSimpleCreateReq;
import com.coigniez.resumebuilder.domain.subsection.dtos.SubSectionUpdateReq;
import com.coigniez.resumebuilder.interfaces.Mapper;
import com.coigniez.resumebuilder.util.MapperUtils;

import jakarta.validation.Valid;

@Service
public class SubSectionMapper
        implements Mapper<SubSection, SubSectionSimpleCreateReq, SubSectionUpdateReq, SubSectionResp> {

    private static final Map<String, Object> DEFAULT_VALUES = Map.of(
        "showTitle", true);

    @Autowired
    private SectionItemMapper sectionItemMapper;

    @Override
    public SubSection toEntity(@Valid SubSectionSimpleCreateReq request) {
        if (request == null) {
            return null;
        }

        MapperUtils.setDefaultValues(request, DEFAULT_VALUES);

        return SubSection.builder()
                .title(request.getTitle())
                .icon(request.getIcon())
                .showTitle(request.getShowTitle())
                .items(request.getSectionItems().stream().map(sectionItemMapper::toEntity).toList())
                .build();
    }

    @Override
    public SubSectionResp toDto(SubSection entity) {
        if (entity == null) {
            return null;
        }

        return SubSectionResp.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .icon(entity.getIcon())
                .showTitle(entity.isShowTitle())
                .sectionItems(entity.getItems().stream().map(sectionItemMapper::toDto).toList())
                .build();
    }

    @Override
    public void updateEntity(SubSection entity, @Valid SubSectionUpdateReq request) {
        if (request == null) {
            return;
        }

        entity.setTitle(request.getTitle());
        entity.setIcon(request.getIcon());
        entity.setShowTitle(request.getShowTitle());
    }
}
