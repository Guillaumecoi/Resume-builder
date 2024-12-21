package com.coigniez.resumebuilder.domain.section;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.coigniez.resumebuilder.domain.section.dtos.CreateSectionRequest;
import com.coigniez.resumebuilder.domain.section.dtos.SectionResponse;
import com.coigniez.resumebuilder.domain.section.dtos.UpdateSectionRequest;
import com.coigniez.resumebuilder.domain.sectionitem.SectionItem;
import com.coigniez.resumebuilder.domain.sectionitem.SectionItemMapper;
import com.coigniez.resumebuilder.domain.sectionitem.dtos.SectionItemResponse;
import com.coigniez.resumebuilder.interfaces.Mapper;
import com.coigniez.resumebuilder.util.MapperUtils;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class SectionMapper implements Mapper<Section, CreateSectionRequest, UpdateSectionRequest, SectionResponse> {

    private final SectionItemMapper sectionItemMapper;

     private static final Map<String, Object> DEFAULT_VALUES = Map.of(
            "showTitle", true
     );

    @Override
    public Section toEntity(CreateSectionRequest request) {
        if (request == null) {
            return null;
        }

        MapperUtils.setDefaultValues(request, DEFAULT_VALUES);

        return Section.builder()
                .title(request.getTitle())
                .showTitle(request.getShowTitle())
                .build();
    }

    @Override
    public SectionResponse toDto(Section entity) {
        if (entity == null) {
            return null;
        }

        List<SectionItemResponse> sectionItems = entity.getItems() == null 
            ? List.of()
            : entity.getItems().stream()
                .sorted(Comparator.comparing(SectionItem::getItemOrder)) // Sort items by itemOrder
                .map(sectionItemMapper::toDto)
                .collect(Collectors.toList());

        return SectionResponse.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .showTitle(entity.isShowTitle())
                .sectionItems(sectionItems)
                .build();
    }

    @Override
    public void updateEntity(Section entity, UpdateSectionRequest request) {
        if (request == null) {
            return;
        }

        entity.setTitle(request.getTitle());
        entity.setShowTitle(request.getShowTitle());
    }
}
