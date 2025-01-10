package com.coigniez.resumebuilder.domain.columnholder.page.dtos;

import com.coigniez.resumebuilder.domain.columnholder.dtos.ColumnHolderCreateReq;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.EqualsAndHashCode;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PageCreateReq extends PageSimpleCreateReq implements ColumnHolderCreateReq {

    private long layoutId;

}
