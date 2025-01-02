package com.coigniez.resumebuilder.domain.sectionitem.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class SectionItemCreateReq extends SectionItemSimpleCreateReq {

    @NotNull
    private long subSectionId;

}