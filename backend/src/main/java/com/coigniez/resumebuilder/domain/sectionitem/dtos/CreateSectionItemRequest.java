package com.coigniez.resumebuilder.domain.sectionitem.dtos;

import com.coigniez.resumebuilder.interfaces.CreateRequest;
import com.coigniez.resumebuilder.interfaces.SectionItemData;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateSectionItemRequest implements CreateRequest {

    @NotNull
    private long sectionId;

    @NotNull
    private SectionItemData item;

    private Integer itemOrder;

}