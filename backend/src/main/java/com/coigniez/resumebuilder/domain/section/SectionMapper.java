package com.coigniez.resumebuilder.domain.section;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.coigniez.resumebuilder.domain.sectionitem.SectionItemMapper;
import com.coigniez.resumebuilder.domain.sectionitem.SectionItemResponse;
import com.coigniez.resumebuilder.interfaces.Mapper;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class SectionMapper implements Mapper<Section, SectionRequest, SectionResponse> {

    private final SectionItemMapper sectionItemMapper;

    @Override
    public Section toEntity(SectionRequest request) {
        if (request == null) {
            return null;
        }

        return Section.builder()
                .id(request.getId())
                .title(request.getTitle())
                .showTitle(request.getShowTitle())
                .build();
    }

    @Override
    public SectionResponse toDto(Section entity) {
        if (entity == null) {
            return null;
        }

        List<SectionItemResponse> sectionItems;
        if (entity.getItems() == null) {
            sectionItems = List.of();
        } else {
            sectionItems = entity.getItems().stream()
                .map(sectionItemMapper::toDto)
                .collect(Collectors.toList());
        }

        return SectionResponse.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .showTitle(entity.isShowTitle())
                .sectionItems(sectionItems)
                .build();
    }
    
}
