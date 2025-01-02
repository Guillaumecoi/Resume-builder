package com.coigniez.resumebuilder.domain.section.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class SectionCreateReq extends SectionSimpleCreateReq {

    @NotNull
    private Long resumeId;    

}
