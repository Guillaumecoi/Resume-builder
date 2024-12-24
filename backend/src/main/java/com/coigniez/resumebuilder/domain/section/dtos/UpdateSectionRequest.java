package com.coigniez.resumebuilder.domain.section.dtos;

import java.util.List;

import com.coigniez.resumebuilder.domain.sectionitem.dtos.CreateSectionItemRequest;
import com.coigniez.resumebuilder.domain.sectionitem.dtos.UpdateSectionItemRequest;
import com.coigniez.resumebuilder.interfaces.UpdateRequest;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateSectionRequest implements UpdateRequest {

    @NotNull
    private Long id;

    @NotBlank
    private String title;
    private String icon;
    @NotNull
    private Boolean showTitle;

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private List<CreateSectionItemRequest> createSectionItems;

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private List<UpdateSectionItemRequest> updateSectionItems;

}
