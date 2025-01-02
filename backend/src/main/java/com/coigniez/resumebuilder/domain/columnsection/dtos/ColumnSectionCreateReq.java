package com.coigniez.resumebuilder.domain.columnsection.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class ColumnSectionCreateReq extends ColumnSectionSimpleCreateReq {

    @NotNull
    private long columnId;

}