package com.coigniez.resumebuilder.domain.columnholder.headerfooter.dtos;

import java.util.List;

import com.coigniez.resumebuilder.domain.column.dtos.ColumnSimpleCreateReq;
import com.coigniez.resumebuilder.domain.columnholder.dtos.ColumnHolderCreateRequest;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HeaderFooterCreateRequest implements ColumnHolderCreateRequest {

    private Double height;
    private Boolean repeatOnEveryPage;

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private List<ColumnSimpleCreateReq> columns;
    
}
