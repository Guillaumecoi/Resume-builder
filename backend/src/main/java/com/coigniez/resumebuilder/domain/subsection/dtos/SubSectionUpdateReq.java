package com.coigniez.resumebuilder.domain.subsection.dtos;

import com.coigniez.resumebuilder.interfaces.UpdateRequest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SubSectionUpdateReq implements UpdateRequest {

    @NotNull
    private Long id;
    @NotNull
    private Long sectionId;

    @NotBlank
    @Size(max = 255)
    private String title;
    @Size(max = 50)
    private String icon;
    @NotNull
    private Boolean showTitle;
}