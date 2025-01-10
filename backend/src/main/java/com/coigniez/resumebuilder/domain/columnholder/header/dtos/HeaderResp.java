package com.coigniez.resumebuilder.domain.columnholder.header.dtos;

import com.coigniez.resumebuilder.domain.columnholder.dtos.ColumnHolderResp;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class HeaderResp extends ColumnHolderResp {

    @NotNull
    private Double height;
    @NotNull
    private Boolean repeatOnEveryPage;

}
