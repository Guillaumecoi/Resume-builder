package com.coigniez.resumebuilder.domain.layoutsection.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class LayoutSectionCreateReq extends LayoutSectionSimpleCreateReq {

    @NotNull
    private long columnSectionId;
}