package com.coigniez.resumebuilder.domain.layoutsectionItem.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class LayoutSectionItemCreateReq extends LayoutSectionItemSimpleCreateReq {

    @NotNull
    private long columnSectionId;
    
}
