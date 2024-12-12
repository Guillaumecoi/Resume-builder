package com.coigniez.resumebuilder.domain.layout;

import java.util.List;
import java.util.Set;

import com.coigniez.resumebuilder.domain.column.ColumnResponse;
import com.coigniez.resumebuilder.domain.latex.LatexMethodResponse;
import com.coigniez.resumebuilder.domain.layout.enums.*;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LayoutResponse {

    private Long id;
    private PageSize pageSize;
    private List<ColumnResponse> columns;
    private Integer numberOfColumns;
    private Double columnSeparator;
    private ColorScheme colorScheme;
    private Set<LatexMethodResponse> latexMethods;
    
}