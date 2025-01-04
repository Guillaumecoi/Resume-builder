package com.coigniez.resumebuilder.domain.columnholder.headerfooter.dtos;

import java.util.List;

import com.coigniez.resumebuilder.domain.column.dtos.ColumnResp;
import com.coigniez.resumebuilder.domain.columnholder.dtos.ColumnHolderResp;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HeaderFooterResp implements ColumnHolderResp {

    private Double height;
    private Boolean repeatOnEveryPage;
    
    private List<ColumnResp> columns;

}
