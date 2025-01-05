package com.coigniez.resumebuilder.domain.section;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

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

        Section section = Section.builder()
                .title(request.getTitle())
                .icon(request.getIcon())
                .showTitle(request.getShowTitle())
                .subSections(new ArrayList<>())
                .build();

        // Add subSections
        Optional.ofNullable(request.getSubSections())
                .ifPresent(subSections -> subSections.forEach(subSection -> {
                    section.addSubSection(subSectionMapper.toEntity(subSection));
                }));

        return section;
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
                .subSections(Optional.ofNullable(entity.getSubSections())
                        .map(subSections -> subSections.stream().map(subSectionMapper::toDto).toList())
                        .orElse(Collections.emptyList()))
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
