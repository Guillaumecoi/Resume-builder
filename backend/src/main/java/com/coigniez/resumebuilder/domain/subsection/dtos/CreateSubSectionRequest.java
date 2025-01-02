package com.coigniez.resumebuilder.domain.subsection.dtos;

import java.util.List;

import com.coigniez.resumebuilder.domain.sectionitem.dtos.SectionItemSimpleCreateReq;
import com.coigniez.resumebuilder.interfaces.CreateRequest;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateSubSectionRequest implements CreateRequest {

    @NotNull
    private Long sectionId;

    @NotBlank
    private String title;
    private String icon;
    private Boolean showTitle;

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private List<SectionItemSimpleCreateReq> sectionItems;

}
