package com.coigniez.resumebuilder.latex.generators;

import java.util.List;

import org.springframework.stereotype.Component;

import com.coigniez.resumebuilder.domain.column.LayoutColumn;
import com.coigniez.resumebuilder.domain.columnholder.header.Header;
import com.coigniez.resumebuilder.domain.columnholder.page.LayoutPage;
import com.coigniez.resumebuilder.domain.columnsection.ColumnSection;
import com.coigniez.resumebuilder.domain.latex.dtos.LatexMethodResp;
import com.coigniez.resumebuilder.domain.layout.Layout;
import com.coigniez.resumebuilder.util.StringUtils;

import lombok.AllArgsConstructor;

/**
 * Generates the latex content for the layout
 */
@AllArgsConstructor
@Component
public class LatexContentGenerator {

    private final StringUtils stringUtils;
    private final LatexSectionGenerator latexSectionGenerator;

    public String generate(Layout layout, LatexMethodResp columnMethod, LatexMethodResp sectionMethod) {
        StringBuilder result = new StringBuilder();
        Header header = layout.getHeader();
        int page = 1;
        for (LayoutPage layoutPage : layout.getPages()) {
            float height = 1.0f;
            if (page > 1 && header.getRepeatOnEveryPage()) {
                result.append(generatePage(header.getColumns(), columnMethod, sectionMethod, header.getHeight()));
                height -= header.getHeight();
            }
            result.append(generatePage(layoutPage.getColumns(), columnMethod, sectionMethod, height));
        }

        return result.toString();
    }

    private String generatePage(List<LayoutColumn> columns, LatexMethodResp columnMethod, LatexMethodResp sectionMethod, float height) {
        StringBuilder content = new StringBuilder();
        content.append(getColumnRatios(columns.stream().map(LayoutColumn::getColumnSize).toList()));
        content.append("\\begin{paracol}{%s}\n\n".formatted(columns.size()));

        for (LayoutColumn column : columns) {
            content.append(getColumn(column, columnMethod, sectionMethod));
        }

        content.append("\\end{paracol}\n");
        return content.toString();
    }

    private String getColumn(LayoutColumn column,  LatexMethodResp columnMethod, LatexMethodResp sectionMethod) {

        // Get the column environment
        String result = "\\switchcolumn[%d]\n".formatted(column.getColumnNumber() - 1) + "\n";
        result += LatexMethodGenerator.generateUsage(columnMethod.getMethodType(), columnMethod.getType(), columnMethod.getName(),
                column.getData());

        // Get the content of the column
        String content = "";
        for (ColumnSection columnSection : column.getSectionMappings()) {
            content += stringUtils.addTabToEachLine(latexSectionGenerator.generate(columnSection, sectionMethod), 1) + "\n";
        }

        return result.formatted(content);
    }

    private String getColumnRatios(List<Float> columnRatios) {
        float total = columnRatios.stream().reduce(0.0f, Float::sum);
        for (int i = 0; i < columnRatios.size(); i++) {
            columnRatios.set(i, columnRatios.get(i) / total);
        }

        String ratio = String.join(",", columnRatios.stream().map(String::valueOf).toArray(String[]::new));

        return "\\columnratio{%s}".formatted(ratio);
    }

}