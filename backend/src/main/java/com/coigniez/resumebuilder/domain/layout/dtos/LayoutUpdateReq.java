package com.coigniez.resumebuilder.domain.layout.dtos;

import java.util.Set;

import com.coigniez.resumebuilder.domain.latex.dtos.LatexMethodCreateReq;
import com.coigniez.resumebuilder.domain.layout.embedded.ColorScheme;
import com.coigniez.resumebuilder.domain.layout.enums.*;
import com.coigniez.resumebuilder.interfaces.UpdateRequest;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LayoutUpdateReq implements UpdateRequest {

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
    private Set<LatexMethodCreateReq> latexMethods;

}