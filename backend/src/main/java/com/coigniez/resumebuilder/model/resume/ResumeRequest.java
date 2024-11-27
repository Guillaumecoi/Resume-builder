package com.coigniez.resumebuilder.model.resume;

import java.util.List;

import com.coigniez.resumebuilder.model.section.SectionRequest;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record ResumeRequest(
    @NotNull
    @NotEmpty
    String title,
    String firstName,
    String lastName,
    List<SectionRequest> sections
) {}
