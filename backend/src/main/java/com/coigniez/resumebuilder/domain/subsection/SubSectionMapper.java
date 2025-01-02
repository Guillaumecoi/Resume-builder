package com.coigniez.resumebuilder.domain.subsection;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coigniez.resumebuilder.domain.sectionitem.SectionItemMapper;
import com.coigniez.resumebuilder.domain.subsection.dtos.CreateSubSectionRequest;
import com.coigniez.resumebuilder.domain.subsection.dtos.SubSectionResponse;
import com.coigniez.resumebuilder.domain.subsection.dtos.UpdateSubSectionRequest;
import com.coigniez.resumebuilder.interfaces.Mapper;
import com.coigniez.resumebuilder.util.MapperUtils;

import jakarta.validation.Valid;

@Service
public class SubSectionMapper
        implements Mapper<SubSection, CreateSubSectionRequest, UpdateSubSectionRequest, SubSectionResponse> {

    private static final Map<String, Object> DEFAULT_VALUES = Map.of(
        "showTitle", true);

    @Autowired
    private SectionItemMapper sectionItemMapper;

    @Override
    public SubSection toEntity(@Valid CreateSubSectionRequest request) {
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
    public SubSectionResponse toDto(SubSection entity) {
        if (entity == null) {
            return null;
        }

        return SubSectionResponse.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .icon(entity.getIcon())
                .showTitle(entity.isShowTitle())
                .sectionItems(entity.getItems().stream().map(sectionItemMapper::toDto).toList())
                .build();
    }

    @Override
    public void updateEntity(SubSection entity, @Valid UpdateSubSectionRequest request) {
        if (request == null) {
            return;
        }

        entity.setTitle(request.getTitle());
        entity.setIcon(request.getIcon());
        entity.setShowTitle(request.getShowTitle());
    }

}
