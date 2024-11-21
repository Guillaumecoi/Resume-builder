package com.coigniez.resumebuilder.model.resume;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record ResumeRequest(
    @NotNull
    @NotEmpty
    String title,
    String firstName,
    String lastName
) {}
