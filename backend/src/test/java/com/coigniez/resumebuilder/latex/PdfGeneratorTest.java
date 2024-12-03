package com.coigniez.resumebuilder.latex;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
public class PdfGeneratorTest {

    @Autowired
    private PdfGenerator pdfGenerator;

    @Value("${application.file.uploads.pdf-output-path}")
    private String pdfOutputPath;

    @BeforeEach
    void setUp() {
        // Ensure the output directory exists
        Path outputPath = Path.of(pdfOutputPath);
        if (!Files.exists(outputPath)) {
            try {
                Files.createDirectories(outputPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    void testGeneratePdf() throws IOException, InterruptedException {
        // Arrange
        String latexContent = """
            \\documentclass[a4paper,10pt]{article}
            \\usepackage[utf8]{inputenc}
            \\usepackage{geometry}
            \\geometry{a4paper, margin=1in}
    
            \\title{Sample Resume}
            \\author{John Doe}
            \\date{\\today}
    
            \\begin{document}
            \\maketitle
    
            \\end{document}
            """;
        String fileName = "test_document";
    
        // Act
        File generatedPdf = pdfGenerator.generatePdf(latexContent, fileName);
    
        // Assert
        assertTrue(generatedPdf.exists(), "The PDF file should be generated.");
        assertTrue(generatedPdf.getName().equals(fileName + ".pdf"), "The PDF file should have the correct name.");
        assertTrue(generatedPdf.length() > 0, "PDF file should not be empty");
    
        // Clean up
        Files.deleteIfExists(generatedPdf.toPath());
    }
}