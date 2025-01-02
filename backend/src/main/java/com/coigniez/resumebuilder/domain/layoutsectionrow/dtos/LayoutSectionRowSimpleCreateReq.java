package com.coigniez.resumebuilder.domain.layoutsectionrow.dtos;

import java.util.List;

import com.coigniez.resumebuilder.domain.layoutsubsection.dtos.LayoutSubSectionSimpleCreateReq;
import com.coigniez.resumebuilder.interfaces.CreateRequest;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class LayoutSectionRowSimpleCreateReq implements CreateRequest {

    private Long latexMethodId;
    private Integer rowOrder;

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private List<LayoutSubSectionSimpleCreateReq> columns;
}