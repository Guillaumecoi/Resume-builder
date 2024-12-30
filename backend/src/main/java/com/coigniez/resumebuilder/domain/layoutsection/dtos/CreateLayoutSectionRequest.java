package com.coigniez.resumebuilder.domain.layoutsection.dtos;

import com.coigniez.resumebuilder.interfaces.CreateRequest;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateLayoutSectionRequest implements CreateRequest {

    @NotNull
    private long columnSectionId;
    @NotNull
    private long sectionId;
    private Long latexMethodId;
}