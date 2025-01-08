package com.coigniez.resumebuilder.domain.columnholder.headerfooter.dtos;

import com.coigniez.resumebuilder.domain.columnholder.dtos.ColumnHolderUpdateReq;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class HeaderFooterUpdateReq extends ColumnHolderUpdateReq {

    private Long id;
    private Double height;
    private Boolean repeatOnEveryPage;
    
}
