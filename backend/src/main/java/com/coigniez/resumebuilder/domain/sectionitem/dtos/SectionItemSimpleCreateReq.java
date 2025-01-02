package com.coigniez.resumebuilder.domain.sectionitem.dtos;

import com.coigniez.resumebuilder.interfaces.SectionItemData;
import com.coigniez.resumebuilder.interfaces.CreateRequest;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class SectionItemSimpleCreateReq implements CreateRequest {

    @NotNull
    private SectionItemData item;
    @NotNull
    private Integer itemOrder;
}
