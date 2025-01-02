package com.coigniez.resumebuilder.domain.section.dtos;

import java.util.List;

import com.coigniez.resumebuilder.domain.subsection.dtos.SubSectionCreateReq;
import com.coigniez.resumebuilder.interfaces.CreateRequest;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class SectionSimpleCreateReq implements CreateRequest {  

    @NotBlank
    private String title;
    private String icon;
    private Boolean showTitle;

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private List<SubSectionCreateReq> subSections;

}
