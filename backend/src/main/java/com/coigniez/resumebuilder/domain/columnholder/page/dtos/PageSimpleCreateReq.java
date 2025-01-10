package com.coigniez.resumebuilder.domain.columnholder.page.dtos;

import java.util.List;

import com.coigniez.resumebuilder.domain.column.dtos.ColumnSimpleCreateReq;
import com.coigniez.resumebuilder.domain.columnholder.dtos.ColumnHolderSimpleCreateReq;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;

import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class PageSimpleCreateReq implements ColumnHolderSimpleCreateReq {

    @Min(1)
    private int pageNumber;

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private List<ColumnSimpleCreateReq> columns;

}
