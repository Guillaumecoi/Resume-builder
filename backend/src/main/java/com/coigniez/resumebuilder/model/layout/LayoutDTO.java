package com.coigniez.resumebuilder.model.layout;

import java.util.List;

import com.coigniez.resumebuilder.model.layout.column.ColumnDTO;
import com.coigniez.resumebuilder.model.layout.enums.*;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LayoutDTO {
    private Long id;

    @Builder.Default
    private PageSize pageSize = PageSize.A4;

    private List<ColumnDTO> columns;

    @Builder.Default
    @NotNull @Min(1) @Max(2)
    private Integer numberOfColumns = 1;
    @NotNull @Min(1) @Max(2)
    private Double columnSeparator;
    @NotNull
    private ColorScheme colorScheme;
    @NotNull
    private LatexCommands latexCommands;
}