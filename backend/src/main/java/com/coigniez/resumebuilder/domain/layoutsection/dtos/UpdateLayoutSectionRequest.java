package com.coigniez.resumebuilder.domain.layoutsection.dtos;

import com.coigniez.resumebuilder.interfaces.UpdateRequest;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateLayoutSectionRequest implements UpdateRequest {

    @NotNull
    private Long id;

    @NotNull
    private long latexMethodId;
}