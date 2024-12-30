package com.coigniez.resumebuilder.latex.generators;

import java.util.List;

import org.springframework.stereotype.Component;

import com.coigniez.resumebuilder.domain.columnsection.ColumnSection;
import com.coigniez.resumebuilder.domain.layoutsectionItem.LayoutSectionItem;
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
        LatexMethodTemplate sectionMethod = LatexMethodTemplates.getSectionTemplate();

        // Get the section environment
        String sectionString = LatexMethodGenerator.generateUsage(sectionMethod.getMethodType(),
                sectionMethod.getType(), sectionMethod.getMethodName(), columnSection.getData());

        // Sort the items
        List<LayoutSectionItem> items = columnSection.getLayoutSectionItems();
        if (columnSection.isDefaultOrder()){
            items.sort((a, b) -> a.getSectionItem().getItemOrder() - b.getSectionItem().getItemOrder());
        } else {
            items.sort((a, b) -> a.getItemOrder() - b.getItemOrder());
        }

        // Set the items
        String itemsString = "";
        for (LayoutSectionItem item : columnSection.getLayoutSectionItems()) {
            if (item.isHidden()) {
                continue;
            }
            itemsString += stringUtils.addTabToEachLine(latexItemGenerator.generate(item), 1) + "\n";
        }

        if (itemsString.isEmpty()) {
            return "";
        } else {
            return sectionString.formatted(itemsString);
        }
    }

}
