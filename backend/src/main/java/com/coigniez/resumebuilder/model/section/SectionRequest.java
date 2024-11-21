package com.coigniez.resumebuilder.model.section;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record SectionRequest (
    @NotNull
    @NotEmpty
    String title, 
    Long resumeId
) {}
