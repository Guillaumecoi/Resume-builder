package com.coigniez.resumebuilder.domain.resume.dtos;

import java.util.List;

import com.coigniez.resumebuilder.domain.section.dtos.CreateSectionRequest;
import com.coigniez.resumebuilder.domain.section.dtos.UpdateSectionRequest;
import com.coigniez.resumebuilder.interfaces.UpdateRequest;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateResumeRequest implements UpdateRequest {

    @NotNull
    private Long id;

    @NotBlank
    private String title;

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private List<CreateSectionRequest> createSections;

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private List<UpdateSectionRequest> updateSections;

}
