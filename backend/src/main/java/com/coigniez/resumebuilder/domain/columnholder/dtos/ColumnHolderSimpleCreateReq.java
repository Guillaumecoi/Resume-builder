package com.coigniez.resumebuilder.domain.columnholder.dtos;

import java.util.List;

import com.coigniez.resumebuilder.domain.column.dtos.ColumnSimpleCreateReq;
import com.coigniez.resumebuilder.interfaces.CreateRequest;

public interface ColumnHolderSimpleCreateReq extends CreateRequest {

    List<ColumnSimpleCreateReq> getColumns();

    void setColumns(List<ColumnSimpleCreateReq> columns);

}
