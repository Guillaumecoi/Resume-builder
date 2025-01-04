package com.coigniez.resumebuilder.domain.columnholder.page.dtos;

import java.util.List;

import com.coigniez.resumebuilder.domain.column.dtos.ColumnSimpleCreateReq;
import com.coigniez.resumebuilder.domain.columnholder.dtos.ColumnHolderCreateRequest;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PageCreateRequest implements ColumnHolderCreateRequest {

    private int pageNumber;

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private List<ColumnSimpleCreateReq> columns;
    
}
