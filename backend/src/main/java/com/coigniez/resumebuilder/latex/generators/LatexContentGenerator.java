package com.coigniez.resumebuilder.latex.generators;

import org.springframework.stereotype.Component;

import com.coigniez.resumebuilder.domain.column.ColumnResponse;
import com.coigniez.resumebuilder.domain.columnsection.ColumnSectionResponse;
import com.coigniez.resumebuilder.domain.layout.LayoutResponse;
import com.coigniez.resumebuilder.util.StringUtils;

import lombok.AllArgsConstructor;

/**
 * Generates the latex content for the layout
 */
@AllArgsConstructor
@Component
public class LatexContentGenerator implements LatexGenerator<LayoutResponse> {

    private final StringUtils stringUtils;
    private final LatexSectionGenerator latexSectionGenerator;
    
    public String generate(LayoutResponse layout) {
        StringBuilder content = new StringBuilder();
        content.append("\\begin{paracol}{%s}\n\n".formatted(layout.getNumberOfColumns()));

        for (ColumnResponse columnDto : layout.getColumns()) {
            content.append(getColumn(columnDto));
        }

        content.append("\\end{paracol}\n");
        return content.toString();
    }

    private String getColumn(ColumnResponse columnDto) {
        StringBuilder column = new StringBuilder();
        column.append("\\switchcolumn[%d]\n".formatted(columnDto.getColumnNumber() - 1));
        column.append("\\begin{tcolorbox%d}\n".formatted(columnDto.getColumnNumber()));
        for (ColumnSectionResponse columnSection : columnDto.getSectionMappings()) {
            column.append(stringUtils.addTabToEachLine(latexSectionGenerator.generate(columnSection), 1) + "\n");
        }
        column.append("\\end{tcolorbox%d}\n\n".formatted(columnDto.getColumnNumber()));

        return column.toString();
    }
    
}