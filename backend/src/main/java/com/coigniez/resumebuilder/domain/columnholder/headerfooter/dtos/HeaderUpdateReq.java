package com.coigniez.resumebuilder.domain.columnholder.headerfooter.dtos;

import com.coigniez.resumebuilder.domain.columnholder.dtos.ColumnHolderUpdateReq;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class HeaderUpdateReq extends ColumnHolderUpdateReq {

    private Double height;
    private Boolean repeatOnEveryPage;
    
}
