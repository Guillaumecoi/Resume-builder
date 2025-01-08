package com.coigniez.resumebuilder.domain.columnholder.dtos;

import java.util.List;

import com.coigniez.resumebuilder.domain.column.dtos.ColumnUpdateReq;
import com.coigniez.resumebuilder.interfaces.UpdateRequest;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public abstract class ColumnHolderUpdateReq implements UpdateRequest {

    @NotNull
    private Long id;

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private List<ColumnUpdateReq> columns;

}
