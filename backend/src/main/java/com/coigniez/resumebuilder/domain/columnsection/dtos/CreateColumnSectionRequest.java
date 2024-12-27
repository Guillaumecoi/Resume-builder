package com.coigniez.resumebuilder.domain.columnsection.dtos;

import java.util.List;

import com.coigniez.resumebuilder.domain.layout.enums.AlignmentType;
import com.coigniez.resumebuilder.domain.layoutsectionItem.dtos.CreateLayoutSectionItemRequest;
import com.coigniez.resumebuilder.interfaces.CreateRequest;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateColumnSectionRequest implements CreateRequest {

    @NotNull
    private long columnId;
    @NotNull
    private long sectionId;
    @NotNull
    private long latexMethodId;

    private Integer itemOrder;
    private Double itemsep;
    private Double endsep;
    private AlignmentType alignment;

    private Boolean hidden;
    private Boolean defaultOrder;

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private List<CreateLayoutSectionItemRequest> items;

}