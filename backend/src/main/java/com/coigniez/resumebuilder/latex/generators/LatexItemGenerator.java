package com.coigniez.resumebuilder.latex.generators;

import org.springframework.stereotype.Component;

import com.coigniez.resumebuilder.domain.latex.LatexMethod;
import com.coigniez.resumebuilder.domain.sectionitem.SectionItem;

import lombok.AllArgsConstructor;

/**
 * Generates the latex content for the section item
 */
@AllArgsConstructor
@Component
public class LatexItemGenerator implements LatexGenerator<SectionItem> {

    public String generate(SectionItem item) {
        LatexMethod method = item.getLatexMethod();

        if (method == null) {
            return "";
        }
        return "\\item " + method.generateCommand(item.getItem());
    }
}
