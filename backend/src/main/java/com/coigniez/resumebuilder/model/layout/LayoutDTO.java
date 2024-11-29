package com.coigniez.resumebuilder.model.layout;

import java.util.List;
import com.coigniez.resumebuilder.model.layout.enums.*;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LayoutDTO {
    private Long id;
    private PageSize pageSize;
    private List<ColumnResponse> columns;
    private Integer numberOfColumns;
    private Double columnSeparator;
    private ColorScheme colorScheme;
    private LatexCommands latexCommands;
}