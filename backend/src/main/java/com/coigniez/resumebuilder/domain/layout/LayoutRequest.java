package com.coigniez.resumebuilder.domain.layout;

import java.util.List;
import java.util.Set;

import com.coigniez.resumebuilder.domain.column.ColumnRequest;
import com.coigniez.resumebuilder.domain.latex.LatexMethodRequest;
import com.coigniez.resumebuilder.domain.layout.enums.*;
import com.coigniez.resumebuilder.templates.LayoutTemplate;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LayoutRequest {
    
    private Long id;

    private long resumeId;

    @Builder.Default
    private PageSize pageSize = PageSize.A4;

    private List<ColumnRequest> columns;

    @Builder.Default
    @NotNull @Min(1) @Max(2)
    private Integer numberOfColumns = 1;

    @Builder.Default
    @NotNull @Min(1) @Max(2)
    private Double columnSeparator = 0.35;

    @Builder.Default
    @NotNull
    private ColorScheme colorScheme = LayoutTemplate.getExecutiveSuiteColors();

    @Builder.Default
    @NotNull
    private Set<LatexMethodRequest> latexMethods = LayoutTemplate.getStandardMethods();

    @Builder.Default
    private String sectionTitleMethod = """
            \\newcommand{\\sectiontitle}[1] {
                \\hspace{6pt}\\textbf{\\Large{\\uppercase{#1}}}\\\\[-4pt]
                \\textcolor{%s}{\\rule{80pt}{2pt}}
            }
            """.formatted(ColorLocation.ACCENT.toString());
}