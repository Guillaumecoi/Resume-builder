package com.coigniez.resumebuilder.model.sectionitem;

import org.springframework.stereotype.Service;
import com.coigniez.resumebuilder.interfaces.Mapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SectionItemMapper implements Mapper<SectionItem, SectionItemRequest, SectionItemResponse> {

    @Override
    public SectionItem toEntity(SectionItemRequest request) {
        if (request == null) {
            log.warn("Null SectionItemRequest provided to mapper");
            return null;
        }
        return SectionItem.builder()
                .type(request.type())
                .itemOrder(request.itemOrder())
                .data(request.data())
                .build();
    }

    @Override
    public SectionItemResponse toDto(SectionItem entity) {
        if (entity == null) {
            log.warn("Null SectionItem entity provided to mapper");
            return null;
        }
        return SectionItemResponse.builder()
                .id(entity.getId())
                .type(entity.getType())
                .itemOrder(entity.getItemOrder())
                .data(entity.getData())
                .build();
    }

}