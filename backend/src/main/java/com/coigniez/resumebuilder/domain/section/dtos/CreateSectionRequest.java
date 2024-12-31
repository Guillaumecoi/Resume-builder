package com.coigniez.resumebuilder.domain.section.dtos;

import java.util.List;

import com.coigniez.resumebuilder.domain.subsection.dtos.CreateSubSectionRequest;
import com.coigniez.resumebuilder.interfaces.CreateRequest;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateSectionRequest implements CreateRequest {

    private Long resumeId;    

    @NotBlank
    private String title;
    private String icon;
    private Boolean showTitle;

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private List<CreateSubSectionRequest> subSections;

}
