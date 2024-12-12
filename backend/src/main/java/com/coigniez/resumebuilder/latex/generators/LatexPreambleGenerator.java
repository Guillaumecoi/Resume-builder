package com.coigniez.resumebuilder.latex.generators;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.coigniez.resumebuilder.domain.column.Column;
import com.coigniez.resumebuilder.domain.layout.Layout;
import com.coigniez.resumebuilder.domain.layout.enums.ColorLocation;
import com.coigniez.resumebuilder.domain.layout.enums.ColorScheme;
import com.coigniez.resumebuilder.domain.layout.enums.PageSize;
import com.coigniez.resumebuilder.util.StringUtils;

import lombok.AllArgsConstructor;

/**
 * Generates the latex preamble String from a layout response.
 */
@AllArgsConstructor
@Component
public class LatexPreambleGenerator implements LatexGenerator<Layout> {
    
    private final StringUtils stringUtils;

    public String generate(Layout layout) {
        // Generate the imports
        String latexPreamble = getImports(layout.getPageSize(), layout.getColumnSeparator()) + "\n";
        // Generate the colors
        latexPreamble += getColors(layout.getColorScheme()) + "\n";
        // Add the latex methods
        String latexMethods = layout.getLatexMethods().stream()
                .map(method -> method.generateMethod())
                .collect(Collectors.joining("\n"));
        latexPreamble += getStandardMethods() + "\n";
        latexPreamble += layout.getSectionTitleMethod() + "\n";
        latexPreamble += latexMethods + "\n";
        latexPreamble += getColumnColorboxMethods(layout.getColumns()) + "\n";

        return latexPreamble;
    }

    /**
     * Generates the imports for the latex document.
     * 
     * @param pageSize The page size of the document
     * @param columnsep The column separation of the document
     * @return The imports for the latex document
     */
    private String getImports(PageSize pageSize, Double columnsep) {
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
            \\columnratio{%.3f}
            \\setlength{\\columnsep}{0pt}
            \\renewcommand{\\arraystretch}{1.5}
            \\shadowoffset{0.3pt}\\shadowcolor{black!70}

            """, pageSize.getLatexName(), pageSize.getLatexName(), columnsep);
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

    private String getStandardMethods() {
        return """
            \\newenvironment{cvsection}[3]{
                \\def\\cvsectionvspace{#3}
                \\ifthenelse{\\equal{#1}{}}{}{\\sectiontitle{#1}}
                \\begin{itemize}[left=0pt, itemsep=#2, label={}, topsep=10pt]
                    }{
                \\end{itemize}
                \\vspace{\\cvsectionvspace}
            }
            """;
    }

    /**
     * Generates the tcolorbox environments for each column.
     * 
     * @param columns The columns to generate the tcolorbox environments for
     * @return The tcolorbox environments for each column
     */
    private String getColumnColorboxMethods(List<Column> columns) {
        StringBuilder columnMethods = new StringBuilder();
        for (Column column : columns) {
            String columnContent = String.format("\\newenvironment{tcolorbox%d}[0] { \n", column.getColumnNumber());

            columnContent += stringUtils.addTabToEachLine(
                    getTcolorbox(column.getBackgroundColor().toString(), 
                    column.getTextColor().toString(), 
                    column.getBorderColor().toString(),
                    column.getPaddingTop(), 
                    column.getPaddingBottom(), 
                    column.getPaddingLeft(), 
                    column.getPaddingRight(),
                    column.getBorderLeft(),
                    column.getBorderRight(),
                    column.getBorderTop(),
                    column.getBorderBottom()),
                    1);

            columnContent += """
                            }{
                        \\end{tcolorbox}
                    }
                    """;

            columnMethods.append(columnContent);
        }
        return columnMethods.toString();
    }

    /**
     * Generates the tcolorbox environment for a column.
     * 
     * @param colback The background color of the tcolorbox
     * @param textcolor The text color of the tcolorbox
     * @param bordercolor The border color of the tcolorbox
     * @param top The top padding of the tcolorbox
     * @param bottom The bottom padding of the tcolorbox
     * @param left The left padding of the tcolorbox
     * @param right The right padding of the tcolorbox
     * @param borderLeft The left border width of the tcolorbox
     * @param borderRight The right border width of the tcolorbox
     * @param borderTop The top border width of the tcolorbox
     * @param borderBottom The bottom border width of the tcolorbox
     * @return The tcolorbox environment for a column
     */
    private String getTcolorbox(String colback, String textcolor, String bordercolor, double top, double bottom, double left, double right,
            double borderLeft, double borderRight, double borderTop, double borderBottom) {
        return String.format("""
            \\begin{tcolorbox}[
                colback=%s,
                width=\\linewidth,
                height=\\textheight,
                left=%.1fpt,
                right=%.1fpt,
                top=%.1fpt,
                bottom=%.1fpt,
                arc=0mm,
                boxrule=0pt,
                rightrule=%.1fpt,
                leftrule=%.1fpt,
                toprule=%.1fpt,
                bottomrule=%.1fpt,
                colframe=%s
            ]
            \\color{%s}

            """, colback, left, right, top, bottom,
            borderRight, borderLeft, borderTop, borderBottom,
            bordercolor, textcolor);
    }
}
