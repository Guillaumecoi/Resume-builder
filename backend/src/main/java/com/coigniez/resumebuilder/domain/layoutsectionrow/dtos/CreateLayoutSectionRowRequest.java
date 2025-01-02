package com.coigniez.resumebuilder.domain.layoutsectionrow.dtos;

import java.util.List;

import com.coigniez.resumebuilder.domain.layoutsubsection.dtos.LayoutSubSectionSimpleCreateReq;
import com.coigniez.resumebuilder.interfaces.CreateRequest;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateLayoutSectionRowRequest implements CreateRequest {

    @NotNull
    private long columnSectionId;
    private Long latexMethodId;
    private Long rowOrder;

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private List<LayoutSubSectionSimpleCreateReq> columns;
}