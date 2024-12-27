package com.coigniez.resumebuilder.latex.generators;

import org.springframework.stereotype.Component;

import com.coigniez.resumebuilder.domain.latex.LatexMethod;
import com.coigniez.resumebuilder.domain.sectionitem.SectionItem;
import com.coigniez.resumebuilder.interfaces.LatexGenerator;

import lombok.AllArgsConstructor;

/**
 * Generates the latex content for the section item
 */
@AllArgsConstructor
@Component
public class LatexItemGenerator implements LatexGenerator<SectionItem> {

    public String generate(SectionItem item) {
        LatexMethod method = null; //TODO

        if (method == null) {
            return "";
        }
        return ""; //TODO
        //return "\\item " + LatexMethodGenerator.generateUsage(method.getMethodType(), method.getType(),
        //        method.getName(), method.getMethod(), item.getItem().getData());
    }
}
