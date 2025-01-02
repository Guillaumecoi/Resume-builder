package com.coigniez.resumebuilder.domain.layoutsectionrow.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class LayoutSectionRowCreateReq extends LayoutSectionRowSimpleCreateReq {

    @NotNull
    private long columnSectionId;

}