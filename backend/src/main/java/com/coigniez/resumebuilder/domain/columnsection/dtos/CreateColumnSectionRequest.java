package com.coigniez.resumebuilder.domain.columnsection.dtos;

import java.util.List;

import com.coigniez.resumebuilder.domain.layout.enums.AlignmentType;
import com.coigniez.resumebuilder.domain.layoutsection.dtos.CreateLayoutSectionRequest;
import com.coigniez.resumebuilder.domain.layoutsectionrow.dtos.LayoutSectionRowSimpleCreateReq;
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
    private Long latexMethodId;

    private Integer sectionOrder;
    private Double itemsep;
    private Double endsep;
    private AlignmentType alignment;
    private Boolean hidden;

    private CreateLayoutSectionRequest layoutSection;

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private List<LayoutSectionRowSimpleCreateReq> rows;

}