package com.coigniez.resumebuilder.model.sectionitem;

import org.springframework.stereotype.Service;
import com.coigniez.resumebuilder.interfaces.Mapper;
import com.coigniez.resumebuilder.model.sectionitem.itemtypes.SectionItemType;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SectionItemMapper implements Mapper<SectionItem, SectionItemRequest, SectionItemResponse> {

    @Override
    public SectionItem toEntity(SectionItemRequest request) {
        if (request == null) {
            return null;
        }

        SectionItemType type = SectionItemType.fromString(request.type());
    
        SectionItem sectionItem = SectionItem.builder()
                .type(type) 
                .itemOrder(request.itemOrder())
                .data(request.data())
                .build();
    
        validateSectionItem(sectionItem);
    
        return sectionItem;
    }

    @Override
    public SectionItemResponse toDto(SectionItem entity) {
        if (entity == null) {
            log.warn("Null SectionItem entity provided to mapper");
            return null;
        }
    
        return SectionItemResponse.builder()
            .id(entity.getId())
            .type(entity.getType().name())
            .itemOrder(entity.getItemOrder())
            .data(entity.getData())
            .build();
    }

    private void validateSectionItem(@Valid SectionItem sectionItem) {
        // Spring's @Valid automatically validates nested objects
    }

}