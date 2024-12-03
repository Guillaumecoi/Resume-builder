package com.coigniez.resumebuilder.latex;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Scanner;

@Component
public class PdfGenerator {
    private static final Logger logger = LoggerFactory.getLogger(PdfGenerator.class);

    @Value("${application.file.uploads.pdf-output-path}")
    private String pdfOutputPath;

    public File generatePdf(String latexContent, String fileName) throws IOException, InterruptedException {
        if (latexContent == null || latexContent.isEmpty()) {
            throw new IllegalArgumentException("LaTeX content cannot be null or empty");
        }
        if (fileName == null || fileName.isEmpty()) {
            throw new IllegalArgumentException("File name cannot be null or empty");
        }

        Path tempDir = Files.createTempDirectory("latex");
        try {
            Path texFilePath = tempDir.resolve(fileName + ".tex");
            try (FileWriter writer = new FileWriter(texFilePath.toFile())) {
                writer.write(latexContent);
            }

            ProcessBuilder processBuilder = new ProcessBuilder(
                    "pdflatex",
                    "-interaction=nonstopmode",
                    "-output-directory", tempDir.toString(),
                    texFilePath.toString()
            );
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            StringBuilder output = new StringBuilder();
            try (Scanner scanner = new Scanner(process.getInputStream())) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    output.append(line).append("\n");
                    logger.debug(line);
                }
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                throw new RuntimeException("pdflatex command failed with exit code " + exitCode + "\nOutput: " + output);
            }

            Path pdfFilePath = tempDir.resolve(fileName + ".pdf");
            if (!Files.exists(pdfFilePath)) {
                throw new IOException("PDF file was not generated: " + pdfFilePath + "\nOutput: " + output);
            }

            Path outputPdfPath = Paths.get(pdfOutputPath, fileName + ".pdf");
            Files.createDirectories(outputPdfPath.getParent());
            Files.move(pdfFilePath, outputPdfPath);

            return outputPdfPath.toFile();
        } finally {
            try {
                if (Files.exists(tempDir)) {
                    Files.walk(tempDir)
                         .sorted(Comparator.reverseOrder())
                         .forEach(path -> {
                             try {
                                 Files.delete(path);
                             } catch (IOException e) {
                                 logger.error("Failed to delete temporary file: " + path, e);
                             }
                         });
                }
            } catch (IOException e) {
                logger.error("Failed to clean up temporary directory: " + tempDir, e);
            }
        }
    }
}