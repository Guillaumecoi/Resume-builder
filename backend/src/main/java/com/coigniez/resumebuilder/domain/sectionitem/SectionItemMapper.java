package com.coigniez.resumebuilder.domain.sectionitem;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.coigniez.resumebuilder.interfaces.Mapper;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Service
public class SectionItemMapper implements Mapper<SectionItem, SectionItemRequest, SectionItemResponse> {

    private final Validator validator;
    private final ObjectMapper objectMapper;

    @Override
    public SectionItem toEntity(SectionItemRequest request) {
        if (request == null) {
            return null;
        }

        SectionItemType type = SectionItemType.valueOf(request.type());

        SectionItem sectionItem = SectionItem.builder()
                .type(type) 
                .itemOrder(request.itemOrder())
                .data(toDataObject(type, request.data()))
                .build();
    
        return sectionItem;
    }

    @Override
    public SectionItemResponse toDto(SectionItem entity) {
        if (entity == null) {
            log.warn("Null SectionItem entity provided to mapper");
            return null;
        }

        // Convert data object to a map and remove type information
        @SuppressWarnings("unchecked")
        Map<String, Object> data = objectMapper.convertValue(entity.getData(), Map.class);
    
        return SectionItemResponse.builder()
            .id(entity.getId())
            .type(entity.getType().name())
            .itemOrder(entity.getItemOrder())
            .data(data)
            .build();
    }

    public SectionItemData toDataObject(SectionItemType type, Map<String, Object> data) {
        if (type == null || data == null) {
            return null;
        }

        // Add @class type information to the data
        Map<String, Object> dataWithType = new HashMap<>(data);
        dataWithType.put("@class", type.getDataType().getName());

        Object dataObject = objectMapper.convertValue(dataWithType, type.getDataType());
        dataWithType.put("@class", type.getDataType().getName());
    
        // Validate the deserialized data object
        validateDataObject(dataObject);

        return (SectionItemData) dataObject;
    }

    private void validateDataObject(Object dataObject) {
        Set<ConstraintViolation<Object>> violations = validator.validate(dataObject);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}