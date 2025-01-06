package com.coigniez.resumebuilder.domain.section.dtos;

import com.coigniez.resumebuilder.interfaces.UpdateRequest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SectionUpdateReq implements UpdateRequest {

    @NotNull
    private Long id;

    @NotBlank
    @Size(max = 255)
    private String title;
    @Size(max = 50)
    private String icon;
    @NotNull
    private Boolean showTitle;

}
