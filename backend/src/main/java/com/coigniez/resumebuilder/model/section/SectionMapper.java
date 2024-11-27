package com.coigniez.resumebuilder.model.section;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.coigniez.resumebuilder.interfaces.Mapper;
import com.coigniez.resumebuilder.model.sectionitem.SectionItemMapper;
import com.coigniez.resumebuilder.model.sectionitem.SectionItemResponse;

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
                .title(request.title())
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
                .sectionItems(sectionItems)
                .build();
    }
    
}
