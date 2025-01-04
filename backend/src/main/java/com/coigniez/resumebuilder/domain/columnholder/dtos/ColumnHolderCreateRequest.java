package com.coigniez.resumebuilder.domain.columnholder.dtos;

import java.util.List;

import com.coigniez.resumebuilder.domain.column.dtos.ColumnSimpleCreateReq;
import com.coigniez.resumebuilder.interfaces.CreateRequest;

public interface ColumnHolderCreateRequest extends CreateRequest {

    public List<ColumnSimpleCreateReq> getColumns();

    public void setColumns(List<ColumnSimpleCreateReq> columns);

}
