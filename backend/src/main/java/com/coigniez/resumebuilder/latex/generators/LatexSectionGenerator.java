package com.coigniez.resumebuilder.latex.generators;

import org.springframework.stereotype.Component;

import com.coigniez.resumebuilder.domain.columnsection.ColumnSection;
import com.coigniez.resumebuilder.domain.section.Section;
import com.coigniez.resumebuilder.domain.sectionitem.SectionItem;
import com.coigniez.resumebuilder.util.StringUtils;

import lombok.AllArgsConstructor;

/**
 * Generates the latex content for the section
 */
@AllArgsConstructor
@Component
public class LatexSectionGenerator implements LatexGenerator<ColumnSection> {

    private final StringUtils stringUtils;
    private final LatexItemGenerator latexItemGenerator;

    public String generate(ColumnSection columnSection) {
        Section section = columnSection.getSection();
        StringBuilder sectionString = new StringBuilder();

        String title = section.isShowTitle() ? section.getTitle() : "";

        // Start the section
        sectionString.append("\\begin{cvsection}{%s}{%.1fpt}{%.1fpt}{%s}\n"
                .formatted(title, columnSection.getItemsep(), columnSection.getEndsep(), ""));

        // Add the items
        for (SectionItem item : section.getItems()) {
            sectionString.append(stringUtils.addTabToEachLine(latexItemGenerator.generate(item), 1) + "\n");
        }

        // End the section
        sectionString.append("\\end{cvsection}");

        return sectionString.toString();
    }
    
}
