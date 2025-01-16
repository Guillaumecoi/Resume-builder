package com.coigniez.resumebuilder.latex.generators;

import org.springframework.stereotype.Component;

import com.coigniez.resumebuilder.domain.column.LayoutColumn;
import com.coigniez.resumebuilder.domain.columnholder.page.LayoutPage;
import com.coigniez.resumebuilder.domain.columnsection.ColumnSection;
import com.coigniez.resumebuilder.domain.layout.Layout;
import com.coigniez.resumebuilder.interfaces.LatexGenerator;
import com.coigniez.resumebuilder.util.StringUtils;

import lombok.AllArgsConstructor;

/**
 * Generates the latex content for the layout
 */
@AllArgsConstructor
@Component
public class LatexContentGenerator implements LatexGenerator<Layout> {

    private final StringUtils stringUtils;
    private final LatexSectionGenerator latexSectionGenerator;
    
    public String generate(Layout layout) {
        StringBuilder result = new StringBuilder();
        layout.getPages().forEach(page -> {
            result.append(generatePage(page));
        });

        return result.toString();
    }

    private String generatePage(LayoutPage layoutPage) {
        StringBuilder content = new StringBuilder();
        content.append("\\begin{paracol}{%s}\n\n".formatted(layoutPage.getColumns().size()));

        for (LayoutColumn column : layoutPage.getColumns()) {
            content.append(getColumn(column));
        }

        content.append("\\end{paracol}\n");
        return content.toString();
    }

    private String getColumn(LayoutColumn column) {
        StringBuilder result = new StringBuilder();
        result.append("\\switchcolumn[%d]\n".formatted(column.getColumnNumber() - 1));
        result.append("\\begin{tcolorbox%d}\n".formatted(column.getColumnNumber()));
        for (ColumnSection columnSection : column.getSectionMappings()) {
            result.append(stringUtils.addTabToEachLine(latexSectionGenerator.generate(columnSection), 1) + "\n");
        }
        result.append("\\end{tcolorbox%d}\n\n".formatted(column.getColumnNumber()));

        return result.toString();
    }
    
}