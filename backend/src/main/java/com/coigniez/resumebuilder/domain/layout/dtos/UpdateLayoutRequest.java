package com.coigniez.resumebuilder.domain.layout.dtos;

import java.util.List;
import java.util.Set;

import com.coigniez.resumebuilder.domain.column.dtos.CreateColumnRequest;
import com.coigniez.resumebuilder.domain.column.dtos.UpdateColumnRequest;
import com.coigniez.resumebuilder.domain.latex.dtos.CreateLatexMethodRequest;
import com.coigniez.resumebuilder.domain.layout.enums.*;
import com.coigniez.resumebuilder.interfaces.UpdateRequest;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateLayoutRequest implements UpdateRequest {

    @NotNull
    private Long id;

    @NotNull
    private PageSize pageSize;

    @NotNull
    @Min(1)
    @Max(2)
    private Integer numberOfColumns;

    @NotNull
    @Min(0)
    @Max(1)
    private Double columnSeparator;

    @NotNull
    private ColorScheme colorScheme;

    @NotNull
    private Set<CreateLatexMethodRequest> latexMethods;

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private List<CreateColumnRequest> CreateColumns;

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private List<UpdateColumnRequest> updateColumns;

}