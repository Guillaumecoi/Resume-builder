package com.coigniez.resumebuilder.domain.sectionitem;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.coigniez.resumebuilder.domain.sectionitem.dtos.SectionItemSimpleCreateReq;
import com.coigniez.resumebuilder.domain.sectionitem.dtos.SectionItemResp;
import com.coigniez.resumebuilder.domain.sectionitem.dtos.SectionItemUpdateReq;
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
public class SectionItemMapper implements Mapper<SectionItem, SectionItemSimpleCreateReq, SectionItemUpdateReq, SectionItemResp> {

    private final Validator validator;

    @Override
    public SectionItem toEntity(SectionItemSimpleCreateReq request) {
        if (request == null) {
            return null;
        }

        Set<ConstraintViolation<SectionItemSimpleCreateReq>> violations = validator.validate(request);
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
    public SectionItemResp toDto(SectionItem entity) {
        if (entity == null) {
            return null;
        }
    
        return SectionItemResp.builder()
            .id(entity.getId())
            .item(entity.getItem())
            .itemOrder(entity.getItemOrder())
            .build();
    }

    @Override
    public void updateEntity(SectionItem entity, SectionItemUpdateReq request) {
        if (request == null) {
            return;
        }

        entity.setItem(request.getItem());
        entity.setItemOrder(request.getItemOrder());
    }
}