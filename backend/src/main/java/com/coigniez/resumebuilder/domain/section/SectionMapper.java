package com.coigniez.resumebuilder.domain.section;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.coigniez.resumebuilder.domain.sectionitem.SectionItem;
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
    public void updateEntity(Section entity, SectionRequest request) {
        if (request == null) {
            return;
        }

        entity.setTitle(request.getTitle());
        entity.setShowTitle(request.getShowTitle());
    }
}
