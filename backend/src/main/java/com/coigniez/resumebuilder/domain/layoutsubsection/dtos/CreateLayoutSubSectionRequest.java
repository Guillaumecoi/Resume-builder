package com.coigniez.resumebuilder.domain.layoutsubsection.dtos;

import java.util.List;

import com.coigniez.resumebuilder.domain.layout.enums.AlignmentType;
import com.coigniez.resumebuilder.domain.layoutsectionItem.dtos.CreateLayoutSectionItemRequest;
import com.coigniez.resumebuilder.interfaces.CreateRequest;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;

import jakarta.validation.constraints.NotNull;
import lombok.*;


@Data
@Builder
public class CreateLayoutSubSectionRequest implements CreateRequest {

    @NotNull
    private long rowId;
    @NotNull
    private long subSectionId;
    private Long latexMethodId;

    private Integer sectionOrder;
    private AlignmentType alignment;

    private Boolean hidden;
    private Boolean defaultOrder;

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private List<CreateLayoutSectionItemRequest> items;

}
