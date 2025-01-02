package com.coigniez.resumebuilder.domain.section;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coigniez.resumebuilder.domain.section.dtos.SectionResp;
import com.coigniez.resumebuilder.domain.section.dtos.SectionSimpleCreateReq;
import com.coigniez.resumebuilder.domain.section.dtos.SectionUpdateReq;
import com.coigniez.resumebuilder.domain.subsection.SubSectionMapper;
import com.coigniez.resumebuilder.interfaces.Mapper;
import com.coigniez.resumebuilder.util.MapperUtils;

@Service
public class SectionMapper implements Mapper<Section, SectionSimpleCreateReq, SectionUpdateReq, SectionResp> {

    private static final Map<String, Object> DEFAULT_VALUES = Map.of(
            "showTitle", true);

    @Autowired
    private SubSectionMapper subSectionMapper;

    @Override
    public Section toEntity(SectionSimpleCreateReq request) {
        if (request == null) {
            return null;
        }

        MapperUtils.setDefaultValues(request, DEFAULT_VALUES);

        return Section.builder()
                .title(request.getTitle())
                .icon(request.getIcon())
                .showTitle(request.getShowTitle())
                .subSections(request.getSubSections().stream().map(subSectionMapper::toEntity).toList())
                .build();
    }

    @Override
    public SectionResp toDto(Section entity) {
        if (entity == null) {
            return null;
        }

        return SectionResp.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .showTitle(entity.isShowTitle())
                .subSections(entity.getSubSections().stream().map(subSectionMapper::toDto).toList())
                .build();
    }

    @Override
    public void updateEntity(Section entity, SectionUpdateReq request) {
        if (request == null) {
            return;
        }

        entity.setTitle(request.getTitle());
        entity.setIcon(request.getIcon());
        entity.setShowTitle(request.getShowTitle());
    }
}
