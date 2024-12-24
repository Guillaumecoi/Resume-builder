package com.coigniez.resumebuilder.latex.generators;

import java.io.IOException;

import org.springframework.stereotype.Service;

import com.coigniez.resumebuilder.domain.layout.Layout;
import com.coigniez.resumebuilder.interfaces.LatexGenerator;
import com.coigniez.resumebuilder.latex.PdfGenerator;
import com.coigniez.resumebuilder.util.StringUtils;

import lombok.AllArgsConstructor;

/**
 * Generates the latex document String from a layout response.
 */
@AllArgsConstructor
@Service
public class LatexDocumentGenerator implements LatexGenerator<Layout> {

    private final LatexPreambleGenerator preambleGenerator;
    private final LatexContentGenerator contentGenerator;
    private final PdfGenerator pdfGenerator;
    private final StringUtils stringUtils;
    
    public String generate(Layout layout) {
        // Generate the preamble of the latex document
        String document = preambleGenerator.generate(layout) + "\n";
        // Start the document
        document += "\\begin{document}\n";
        // Generate the content of the latex document
        document += stringUtils.addTabToEachLine(contentGenerator.generate(layout), 1) + "\n";
        // End the document
        document += "\\end{document}";

        return document;
    }

    public byte[] generateFile(Layout layout, String filename) throws IOException, InterruptedException {
        String document = generate(layout);
        return pdfGenerator.generatePdf(document, filename);
    }
}
