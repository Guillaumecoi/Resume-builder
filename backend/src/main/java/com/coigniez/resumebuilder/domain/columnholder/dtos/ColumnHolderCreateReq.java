package com.coigniez.resumebuilder.domain.columnholder.dtos;

import java.util.List;

import com.coigniez.resumebuilder.domain.column.dtos.ColumnSimpleCreateReq;
import com.coigniez.resumebuilder.interfaces.CreateRequest;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public abstract class ColumnHolderCreateReq implements CreateRequest {

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private List<ColumnSimpleCreateReq> columns;

}
