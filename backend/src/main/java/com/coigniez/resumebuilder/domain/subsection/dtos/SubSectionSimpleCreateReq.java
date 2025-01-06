package com.coigniez.resumebuilder.domain.subsection.dtos;

import java.util.List;

import com.coigniez.resumebuilder.domain.sectionitem.dtos.SectionItemSimpleCreateReq;
import com.coigniez.resumebuilder.interfaces.CreateRequest;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class SubSectionSimpleCreateReq implements CreateRequest {

    @NotBlank
    @Size(max = 255)
    private String title;
    @Size(max = 50)
    private String icon;
    private Boolean showTitle;

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private List<SectionItemSimpleCreateReq> sectionItems;

}
