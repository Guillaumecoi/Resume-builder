package com.coigniez.resumebuilder.model.section;

import java.util.List;

import com.coigniez.resumebuilder.model.sectionitem.SectionItemRequest;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record SectionRequest (
    Long id,
    @NotNull
    @NotEmpty
    String title,
    List<SectionItemRequest> sectionItems

) {}
