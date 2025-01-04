package com.coigniez.resumebuilder.domain.columnholder.page.dtos;

import java.util.List;

import com.coigniez.resumebuilder.domain.column.dtos.ColumnResp;
import com.coigniez.resumebuilder.domain.columnholder.dtos.ColumnHolderResp;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PageResp implements ColumnHolderResp {

    private int pageNumber;
    
    private List<ColumnResp> columns;
    
}
