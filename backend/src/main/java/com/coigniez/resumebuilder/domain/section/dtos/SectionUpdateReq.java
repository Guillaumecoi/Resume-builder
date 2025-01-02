package com.coigniez.resumebuilder.domain.section.dtos;

import com.coigniez.resumebuilder.interfaces.UpdateRequest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SectionUpdateReq implements UpdateRequest {

    @NotNull
    private Long id;

    @NotBlank
    private String title;
    private String icon;
    @NotNull
    private Boolean showTitle;

}
