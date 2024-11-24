package com.coigniez.resumebuilder.model.sectionitem;

import java.util.Map;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record SectionItemRequest(
    @NotNull
    @NotEmpty
    String type,
    Integer itemOrder,
    @NotNull
    Map<String, Object> data
) {}