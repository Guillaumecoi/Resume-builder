package com.coigniez.resumebuilder.latex.generators;

import org.springframework.stereotype.Component;

import com.coigniez.resumebuilder.domain.columnsection.ColumnSectionResponse;
import com.coigniez.resumebuilder.domain.section.SectionResponse;
import com.coigniez.resumebuilder.domain.sectionitem.SectionItemResponse;
import com.coigniez.resumebuilder.util.StringUtils;

import lombok.AllArgsConstructor;

/**
 * Generates the latex content for the section
 */
@AllArgsConstructor
@Component
public class LatexSectionGenerator implements LatexGenerator<ColumnSectionResponse> {

    private final StringUtils stringUtils;
    private final LatexItemGenerator latexItemGenerator;

    public String generate(ColumnSectionResponse columnSection) {
        SectionResponse section = columnSection.getSection();
        StringBuilder sectionString = new StringBuilder();

        String title = section.isShowTitle() ? section.getTitle() : "";

        // Start the section
        sectionString.append("\\begin{cvsection}{%s}{%.1fpt}{%.1fpt}{%s}\n"
                .formatted(title, columnSection.getItemsep(), columnSection.getEndsep(), ""));

        // Add the items
        for (SectionItemResponse item : section.getSectionItems()) {
            sectionString.append(stringUtils.addTabToEachLine(latexItemGenerator.generate(item), 1) + "\n");
        }

        // End the section
        sectionString.append("\\end{cvsection}");

        return sectionString.toString();
    }
    
}
