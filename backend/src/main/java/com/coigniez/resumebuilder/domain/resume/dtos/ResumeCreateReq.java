package com.coigniez.resumebuilder.domain.resume.dtos;

import java.util.List;

import com.coigniez.resumebuilder.domain.section.dtos.SectionSimpleCreateReq;
import com.coigniez.resumebuilder.interfaces.CreateRequest;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResumeCreateReq implements CreateRequest {

    @NotBlank
    @Size(max = 255)
    private String title;

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private List<SectionSimpleCreateReq> sections;
}
