package com.coigniez.resumebuilder.domain.sectionitem;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.coigniez.resumebuilder.domain.sectionitem.dtos.CreateSectionItemRequest;
import com.coigniez.resumebuilder.domain.sectionitem.dtos.SectionItemResponse;
import com.coigniez.resumebuilder.domain.sectionitem.dtos.UpdateSectionItemRequest;
import com.coigniez.resumebuilder.interfaces.Mapper;
import com.coigniez.resumebuilder.interfaces.SectionItemData;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Service
public class SectionItemMapper implements Mapper<SectionItem, CreateSectionItemRequest, UpdateSectionItemRequest, SectionItemResponse> {

    private final Validator validator;

    @Override
    public SectionItem toEntity(CreateSectionItemRequest request) {
        if (request == null) {
            return null;
        }

        Set<ConstraintViolation<CreateSectionItemRequest>> violations = validator.validate(request);
        Set<ConstraintViolation<SectionItemData>> itemViolations = Collections.emptySet();

        if (request.getItem() != null) {
            itemViolations = validator.validate(request.getItem());
        }

        if (!violations.isEmpty() || !itemViolations.isEmpty()) {
            Set<ConstraintViolation<?>> allViolations = new HashSet<>();
            allViolations.addAll(violations);
            allViolations.addAll(itemViolations);
            throw new ConstraintViolationException(allViolations);
        }

        SectionItem sectionItem = SectionItem.builder()
                .item(request.getItem())
                .itemOrder(request.getItemOrder())
                .build();
    
        return sectionItem;
    }

    @Override
    public SectionItemResponse toDto(SectionItem entity) {
        if (entity == null) {
            return null;
        }
    
        return SectionItemResponse.builder()
            .id(entity.getId())
            .item(entity.getItem())
            .itemOrder(entity.getItemOrder())
            .build();
    }

    @Override
    public void updateEntity(SectionItem entity, UpdateSectionItemRequest request) {
        if (request == null) {
            return;
        }

        entity.setItem(request.getItem());
        entity.setItemOrder(request.getItemOrder());
    }
}