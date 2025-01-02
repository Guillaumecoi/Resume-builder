package com.coigniez.resumebuilder.domain.layoutsubsection.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class LayoutSubSectionCreateReq extends LayoutSubSectionSimpleCreateReq {

    @NotNull
    private long rowId;

}
