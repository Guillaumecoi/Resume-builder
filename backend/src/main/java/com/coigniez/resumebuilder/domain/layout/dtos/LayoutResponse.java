package com.coigniez.resumebuilder.domain.layout.dtos;

import java.util.List;
import java.util.Set;

import com.coigniez.resumebuilder.domain.column.dtos.ColumnResponse;
import com.coigniez.resumebuilder.domain.latex.dtos.LatexMethodResponse;
import com.coigniez.resumebuilder.domain.layout.enums.*;
import com.coigniez.resumebuilder.interfaces.Response;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LayoutResponse implements Response {

    @NotNull
    private Long id;
    @NotNull
    private PageSize pageSize;
    @NotNull
    private List<ColumnResponse> columns;
    @NotNull
    private int numberOfColumns;
    @NotNull
    private double columnSeparator;
    @NotNull
    private ColorScheme colorScheme;
    @NotNull
    private Set<LatexMethodResponse> latexMethods;
    
}