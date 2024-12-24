package com.coigniez.resumebuilder.domain.layout.dtos;

import java.util.List;
import java.util.Set;

import com.coigniez.resumebuilder.domain.column.dtos.CreateColumnRequest;
import com.coigniez.resumebuilder.domain.latex.dtos.CreateLatexMethodRequest;
import com.coigniez.resumebuilder.domain.layout.enums.*;
import com.coigniez.resumebuilder.interfaces.CreateRequest;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateLayoutRequest implements CreateRequest {

    @NotNull
    private long resumeId;

    private PageSize pageSize;

    @Min(1) @Max(2)
    private Integer numberOfColumns;
    @Min(0) @Max(1)
    private Double columnSeparator;

    private ColorScheme colorScheme;
    private Set<CreateLatexMethodRequest> latexMethods;

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private List<CreateColumnRequest> columns;
}