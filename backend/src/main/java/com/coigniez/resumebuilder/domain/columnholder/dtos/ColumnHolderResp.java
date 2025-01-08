package com.coigniez.resumebuilder.domain.columnholder.dtos;
import java.util.List;

import com.coigniez.resumebuilder.domain.column.dtos.ColumnResp;
import com.coigniez.resumebuilder.interfaces.Response;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public abstract class ColumnHolderResp implements Response {

    @NotNull
    private Long id;

    @NotNull
    private List<ColumnResp> columns;
    
}
