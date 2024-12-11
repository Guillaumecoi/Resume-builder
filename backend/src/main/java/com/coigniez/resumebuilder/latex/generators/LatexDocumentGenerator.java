package com.coigniez.resumebuilder.latex.generators;

import org.springframework.stereotype.Service;

import com.coigniez.resumebuilder.domain.layout.LayoutResponse;
import com.coigniez.resumebuilder.util.StringUtils;

import lombok.AllArgsConstructor;

/**
 * Generates the latex document String from a layout response.
 */
@AllArgsConstructor
@Service
public class LatexDocumentGenerator implements LatexGenerator<LayoutResponse> {
    private final LatexPreambleGenerator preambleGenerator;
    private final LatexContentGenerator contentGenerator;
    private final StringUtils stringUtils;
    
    public String generate(LayoutResponse layout) {
        // Generate the preamble of the latex document
        String document = preambleGenerator.generate(layout);
        // Start the document
        document += "\\begin{document}\n";
        // Generate the content of the latex document
        document += stringUtils.addTabToEachLine(contentGenerator.generate(layout), 1);
        // End the document
        document += "\\end{document}\n";

        return document;
    }
}
