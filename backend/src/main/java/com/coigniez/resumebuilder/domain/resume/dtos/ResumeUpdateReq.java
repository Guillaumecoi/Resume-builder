package com.coigniez.resumebuilder.domain.resume.dtos;

import com.coigniez.resumebuilder.interfaces.UpdateRequest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResumeUpdateReq implements UpdateRequest {

    @NotNull
    private Long id;

    @NotBlank
    @Size(max = 255)
    private String title;
}
