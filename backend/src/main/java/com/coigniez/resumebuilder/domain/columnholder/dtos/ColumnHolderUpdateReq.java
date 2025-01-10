package com.coigniez.resumebuilder.domain.columnholder.dtos;

import java.util.List;

import com.coigniez.resumebuilder.domain.column.dtos.ColumnUpdateReq;
import com.coigniez.resumebuilder.domain.columnholder.header.dtos.HeaderUpdateReq;
import com.coigniez.resumebuilder.domain.columnholder.page.dtos.PageUpdateReq;
import com.coigniez.resumebuilder.interfaces.UpdateRequest;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
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
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = HeaderUpdateReq.class, name = "header"),
    @JsonSubTypes.Type(value = PageUpdateReq.class, name = "page")
})
public abstract class ColumnHolderUpdateReq implements UpdateRequest {

    @NotNull
    private Long id;

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private List<ColumnUpdateReq> columns;

}
