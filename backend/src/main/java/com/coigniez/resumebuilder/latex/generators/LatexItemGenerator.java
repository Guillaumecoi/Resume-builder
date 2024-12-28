package com.coigniez.resumebuilder.latex.generators;

import org.springframework.stereotype.Component;

import com.coigniez.resumebuilder.domain.latex.LatexMethod;
import com.coigniez.resumebuilder.domain.layoutsectionItem.LayoutSectionItem;
import com.coigniez.resumebuilder.interfaces.LatexGenerator;

/**
 * Generates the latex content for the section item
 */
@Component
public class LatexItemGenerator implements LatexGenerator<LayoutSectionItem> {

    public String generate(LayoutSectionItem item) {
        LatexMethod method = item.getLatexMethod();

        if (method == null) {
            return "";
        }

        return "\\item " + LatexMethodGenerator.generateUsage(method.getMethodType(), method.getType(),
                method.getName(), item.getSectionItem().getItem().getData());

    }

}
