package com.coigniez.resumebuilder.latex.generators;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.coigniez.resumebuilder.domain.layout.Layout;
import com.coigniez.resumebuilder.domain.layout.embedded.ColorScheme;
import com.coigniez.resumebuilder.domain.layout.enums.ColorLocation;
import com.coigniez.resumebuilder.domain.layout.enums.PageSize;
import com.coigniez.resumebuilder.interfaces.LatexGenerator;

import lombok.AllArgsConstructor;

/**
 * Generates the latex preamble String from a layout response.
 */
@AllArgsConstructor
@Component
public class LatexPreambleGenerator implements LatexGenerator<Layout> {

    public String generate(Layout layout) {
        // Generate the imports
        String latexPreamble = getImports(layout.getPageSize()) + "\n";
        // Generate the colors
        latexPreamble += getColors(layout.getColorScheme()) + "\n";
        // Add the latex methods
        String latexMethods = layout.getLatexMethods().stream()
                .map(method -> LatexMethodGenerator.generateMethod(method.getMethodType(), method.getType(),
                        method.getName(), method.getMethod()))
                .collect(Collectors.joining("\n"));
        latexPreamble += latexMethods + "\n";

        return latexPreamble;
    }

    /**
     * Generates the imports for the latex document.
     * 
     * @param pageSize  The page size of the document
     * @param columnsep The column separation of the document
     * @return The imports for the latex document
     */
    private String getImports(PageSize pageSize) {
        return String.format("""
                \\documentclass[%s,10pt]{article}
                \\usepackage[utf8]{inputenc}
                \\usepackage{geometry}
                \\geometry{%s,margin=0cm}

                \\usepackage{paracol}
                \\usepackage{xcolor}
                \\usepackage[most]{tcolorbox}
                \\usepackage{tikz}
                \\usepackage{graphicx}
                \\usepackage{fontawesome5}
                \\usepackage[hidelinks]{hyperref}
                \\usepackage{tabularx}
                \\usepackage{enumitem}
                \\usepackage{shadowtext}
                \\usepackage{xifthen}
                \\usepackage{parskip}
                \\usepackage{pgf}
                \\usepackage{pgffor}
                \\usepackage{fp}
                \\usepackage{xfp}
                \\usepackage{setspace}

                %% Libraries for Tikz
                \\usetikzlibrary{shadows}
                \\usetikzlibrary{shadows.blur}

                %% settings
                \\setlength{\\columnsep}{0pt}
                \\renewcommand{\\arraystretch}{1.5}
                \\shadowoffset{0.3pt}\\shadowcolor{black!70}

                """, pageSize.getLatexName(), pageSize.getLatexName());
    }

    /**
     * Generates the colors for the latex document.
     * 
     * @param colorScheme The color scheme of the document
     * @return The colors for the latex document
     */
    private String getColors(ColorScheme colorScheme) {
        StringBuilder colors = new StringBuilder();
        colors.append(getColorLine(ColorLocation.PRIMARY.toString(), colorScheme.getPrimaryColor()));
        colors.append(getColorLine(ColorLocation.SECONDARY.toString(), colorScheme.getSecondaryColor()));
        colors.append(getColorLine(ColorLocation.ACCENT.toString(), colorScheme.getAccent()));
        colors.append(getColorLine(ColorLocation.DARK_BG.toString(), colorScheme.getDarkBg()));
        colors.append(getColorLine(ColorLocation.LIGHT_BG.toString(), colorScheme.getLightBg()));
        colors.append(getColorLine(ColorLocation.DARK_TEXT.toString(), colorScheme.getDarkText()));
        colors.append(getColorLine(ColorLocation.LIGHT_TEXT.toString(), colorScheme.getLightText()));

        return colors.toString();
    }

    private String getColorLine(String colorName, String color) {
        // Use substring to remove the # from the color
        return "\\definecolor{" + colorName + "}{HTML}{" + color.substring(1) + "} \n";
    }
}
