package com.coigniez.resumebuilder.domain.latex.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class LatexMethodCreateReq extends LatexMethodSimpleCreateReq {

    @NotNull
    private long layoutId;

}
