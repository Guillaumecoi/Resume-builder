package com.coigniez.resumebuilder.domain.subsection.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class SubSectionCreateReq extends SubSectionSimpleCreateReq {

    @NotNull
    private Long sectionId;

}
