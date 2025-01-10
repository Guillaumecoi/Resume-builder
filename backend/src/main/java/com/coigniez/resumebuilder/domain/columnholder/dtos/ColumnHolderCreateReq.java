package com.coigniez.resumebuilder.domain.columnholder.dtos;

public interface ColumnHolderCreateReq extends ColumnHolderSimpleCreateReq {

    long getLayoutId();

    void setLayoutId(long layoutId);

}
