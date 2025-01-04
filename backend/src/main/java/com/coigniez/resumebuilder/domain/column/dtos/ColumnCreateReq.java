package com.coigniez.resumebuilder.domain.column.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class ColumnCreateReq extends ColumnSimpleCreateReq {

    @NotNull
    private long columnHolderId;

}
