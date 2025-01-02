package com.coigniez.resumebuilder.domain.section;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coigniez.resumebuilder.domain.section.dtos.CreateSectionRequest;
import com.coigniez.resumebuilder.domain.section.dtos.SectionResponse;
import com.coigniez.resumebuilder.domain.section.dtos.UpdateSectionRequest;
import com.coigniez.resumebuilder.domain.subsection.SubSectionMapper;
import com.coigniez.resumebuilder.interfaces.Mapper;
import com.coigniez.resumebuilder.util.MapperUtils;

@Service
public class SectionMapper implements Mapper<Section, CreateSectionRequest, UpdateSectionRequest, SectionResponse> {

    private static final Map<String, Object> DEFAULT_VALUES = Map.of(
            "showTitle", true);

    @Autowired
    private SubSectionMapper subSectionMapper;

    @Override
    public Section toEntity(CreateSectionRequest request) {
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
    public SectionResponse toDto(Section entity) {
        if (entity == null) {
            return null;
        }

        return SectionResponse.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .showTitle(entity.isShowTitle())
                .subSections(entity.getSubSections().stream().map(subSectionMapper::toDto).toList())
                .build();
    }

    @Override
    public void updateEntity(Section entity, UpdateSectionRequest request) {
        if (request == null) {
            return;
        }

        entity.setTitle(request.getTitle());
        entity.setIcon(request.getIcon());
        entity.setShowTitle(request.getShowTitle());
    }
}
