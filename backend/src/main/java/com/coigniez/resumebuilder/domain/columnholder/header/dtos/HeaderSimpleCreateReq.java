package com.coigniez.resumebuilder.domain.columnholder.header.dtos;

import java.util.List;

import com.coigniez.resumebuilder.domain.column.dtos.ColumnSimpleCreateReq;
import com.coigniez.resumebuilder.domain.columnholder.dtos.ColumnHolderSimpleCreateReq;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class HeaderSimpleCreateReq implements ColumnHolderSimpleCreateReq {

    private Double height;
    private Boolean repeatOnEveryPage;

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private List<ColumnSimpleCreateReq> columns;
    
}
