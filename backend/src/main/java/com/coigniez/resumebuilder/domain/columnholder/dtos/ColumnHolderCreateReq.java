package com.coigniez.resumebuilder.domain.columnholder.dtos;

import com.coigniez.resumebuilder.domain.columnholder.header.dtos.HeaderCreateReq;
import com.coigniez.resumebuilder.domain.columnholder.page.dtos.PageCreateReq;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = HeaderCreateReq.class, name = "header"),
    @JsonSubTypes.Type(value = PageCreateReq.class, name = "page")
})
public interface ColumnHolderCreateReq extends ColumnHolderSimpleCreateReq {

    long getLayoutId();

    void setLayoutId(long layoutId);
}