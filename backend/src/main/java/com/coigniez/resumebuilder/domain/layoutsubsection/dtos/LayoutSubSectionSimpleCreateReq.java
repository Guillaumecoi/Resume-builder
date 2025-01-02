package com.coigniez.resumebuilder.domain.layoutsubsection.dtos;

import com.coigniez.resumebuilder.domain.layout.enums.AlignmentType;
import com.coigniez.resumebuilder.interfaces.CreateRequest;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class LayoutSubSectionSimpleCreateReq implements CreateRequest {

    @NotNull
    private long subSectionId;
    private Long latexMethodId;

    private Integer sectionOrder;
    private AlignmentType alignment;

    private Boolean hidden;
    private Boolean defaultOrder;

}
