package com.coigniez.resumebuilder.domain.columnholder.page.dtos;

import com.coigniez.resumebuilder.domain.columnholder.dtos.ColumnHolderUpdateReq;

import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PageUpdateReq extends ColumnHolderUpdateReq {

    @Min(1)
    private int pageNumber;
    
}
