package com.coigniez.resumebuilder.latex.generators;

import org.springframework.stereotype.Component;

import com.coigniez.resumebuilder.domain.columnsection.ColumnSection;
import com.coigniez.resumebuilder.domain.section.Section;
import com.coigniez.resumebuilder.domain.sectionitem.SectionItem;
import com.coigniez.resumebuilder.interfaces.LatexGenerator;
import com.coigniez.resumebuilder.templates.methods.LatexMethodTemplate;
import com.coigniez.resumebuilder.templates.methods.LatexMethodTemplates;
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
        LatexMethodTemplate sectionMethod = LatexMethodTemplates.getSectionTemplate();

        // Get the section environment
        String sectionString = LatexMethodGenerator.generateUsage(sectionMethod.getMethodType(),
                sectionMethod.getType(), sectionMethod.getMethodName(), sectionMethod.getMethod(), columnSection.getData());

        // Set the items
        String items = "";
        for (SectionItem item : section.getItems()) {
            items += stringUtils.addTabToEachLine(latexItemGenerator.generate(item), 1) + "\n";
        }

        return sectionString.formatted(items);
    }

}
