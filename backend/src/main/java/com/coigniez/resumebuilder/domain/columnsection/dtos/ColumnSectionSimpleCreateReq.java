package com.coigniez.resumebuilder.domain.columnsection.dtos;

import java.util.List;

import com.coigniez.resumebuilder.domain.layout.enums.AlignmentType;
import com.coigniez.resumebuilder.domain.layoutsection.dtos.LayoutSectionCreateReq;
import com.coigniez.resumebuilder.domain.layoutsectionrow.dtos.LayoutSectionRowSimpleCreateReq;
import com.coigniez.resumebuilder.interfaces.CreateRequest;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class ColumnSectionSimpleCreateReq implements CreateRequest {

    private Long latexMethodId;

    private Integer sectionOrder;
    private Double itemsep;
    private Double endsep;
    private AlignmentType alignment;
    private Boolean hidden;

    private LayoutSectionCreateReq layoutSection;

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private List<LayoutSectionRowSimpleCreateReq> rows;

}