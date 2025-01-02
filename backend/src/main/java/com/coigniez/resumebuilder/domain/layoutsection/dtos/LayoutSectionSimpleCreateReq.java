package com.coigniez.resumebuilder.domain.layoutsection.dtos;

import com.coigniez.resumebuilder.interfaces.CreateRequest;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class LayoutSectionSimpleCreateReq implements CreateRequest {

    @NotNull
    private long sectionId;
    private Long latexMethodId;
}