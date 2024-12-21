package com.coigniez.resumebuilder.domain.section.dtos;

import java.util.List;

import com.coigniez.resumebuilder.domain.sectionitem.dtos.CreateSectionItemRequest;
import com.coigniez.resumebuilder.interfaces.CreateRequest;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateSectionRequest implements CreateRequest {

    @NotNull
    long resumeId;

    @NotBlank
    String title;
    Boolean showTitle;

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    List<CreateSectionItemRequest> sectionItems;

}
