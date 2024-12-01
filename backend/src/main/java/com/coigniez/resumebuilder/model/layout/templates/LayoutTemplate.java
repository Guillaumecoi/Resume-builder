package com.coigniez.resumebuilder.model.layout.templates;

import java.util.ArrayList;
import java.util.List;

import com.coigniez.resumebuilder.model.layout.Layout;
import com.coigniez.resumebuilder.model.layout.column.Column;
import com.coigniez.resumebuilder.model.layout.enums.ColorLocation;
import com.coigniez.resumebuilder.model.layout.enums.ColorScheme;
import com.coigniez.resumebuilder.model.layout.enums.LatexCommands;

public final class LayoutTemplate {
    
    private LayoutTemplate() {} // Prevent instantiation
    
    public static Layout getDefaultSingleColumn() {
        return Layout.builder()
                .numberOfColumns(1)
                .columns(getDefaultColumns(1))
                .colorScheme(getExecutiveSuiteColors())
                .latexCommands(getBasicCommands())
                .build();
    }

    public static Layout getDefaultTwoColumn() {
        return Layout.builder()
                .numberOfColumns(2)
                .columns(getDefaultColumns(2))
                .colorScheme(getExecutiveSuiteColors())
                .latexCommands(getBasicCommands())
                .build();
    }

    public static List<Column> getDefaultColumns(int numberOfColumns) {
        List<Column> columns = new ArrayList<>();
        columns.add(Column.builder()
                .columnNumber(1)
                .backgroundColor(ColorLocation.DARK_BG)
                .textColor(ColorLocation.LIGHT_TEXT)
                .paddingBottom(10.0)
                .paddingLeft(10.0)
                .paddingRight(10.0)
                .paddingTop(10.0)
                .build());
        if (numberOfColumns == 2) {
            columns.add(Column.builder()
                    .columnNumber(2)
                    .backgroundColor(ColorLocation.LIGHT_BG)
                    .textColor(ColorLocation.DARK_TEXT)
                    .paddingBottom(10.0)
                    .paddingLeft(10.0)
                    .paddingRight(10.0)
                    .paddingTop(10.0)
                    .build());
        }
        return columns;
    }

    public static ColorScheme getExecutiveSuiteColors() {
        return ColorScheme.builder()
                .name("Executive Suite")
                .primaryColor("#31323C") 
                .secondaryColor("#dad7d5")      
                .accent("#613B2E")          
                .darkBg("#31323C")   
                .lightBg("#E8E8EA")  
                .darkText("#31323C")        
                .lightText("#dad7d5")     
                .build();
    }

    public static LatexCommands getBasicCommands() {
        return LatexCommands.builder()
            .cvTitle("""
                \\newcommand{\\cvtitle}[1] {
                    \\hspace{6pt}\\textbf{\\Large{\\uppercase{#1}}}\\\\[-4pt]
                    \\textcolor{accentcolor}{\\rule{80pt}{2pt}}
                }"""
            )
            .educationItem("""
                \\newcommand{\\educationitem}[3]{
                    \\item
                    \\textbf{#1} \\newline
                    \\textit{#2} \\newline
                    #3
                }"""
            )
            .experienceItem("""
                \\newcommand{\\experienceitem}[5]{
                    \\item
                    \\textbf{\\large#2} \\[4pt]
                    \\textbf{#1} \\hfill \\textit{#3} \\\\[-6pt]
                    \\textcolor{accentcolor}{\\rule{\\linewidth}{0.4pt}} \\\\[2pt]
                    \\small#4
                    \\ifthenelse{\\isempty{#5}}{}{%
                        \\footnotesize{
                            \\begin{itemize}[left=5pt, itemsep=2pt, topsep=6pt]
                                #5
                            \\end{itemize}
                        }
                    }
                }"""
            )
            .skillItem("""
                \\newcommand{\\skillitem}[1]{%
                    \\item
                    \\textbf{#1}
                }"""
            )
            .skilltext("""
                \\newcommand{\\skilltext}[2]{%
                    \\item
                    \\textbf{#1} \\hfill #2
                }"""
            )
            .skillbullets("""
                \\newcommand{\\skillbullets}[2]{%
                    \\item
                    \\textbf{#1} \\hfill
                    \\foreach \\x in {1,2,3,4,5}{%
                        \\pgfmathparse{#2/2>=\\x}%
                        \\ifnum\\pgfmathresult=1
                            \\textcolor{accentcolor}{$\\bullet$}%
                        \\else
                            \\textcolor{lighttextcolor}{$\\circ$}%
                        \\fi
                    }%
                }"""
            )
            .skillbar("""
                \\newcommand{\\skillbar}[2]{%
                    \\item
                    \\textbf{#1} \\hfill
                    \\begin{tikzpicture}[baseline]
                        \\fill[lighttextcolor,rounded corners=2pt] (0,0) rectangle (2,.15);
                        \\fill[accentcolor,rounded corners=2pt] (0,0) rectangle ({2*#2/10},.15);
                    \\end{tikzpicture}
                }"""
            )
            .skillbox("""
                \\newcommand{\\skillbox}[1]{%
                    \\tcbox[
                        enhanced,
                        nobeforeafter,
                        size=minimal,
                        height=1.4em,
                        colback=accentcolor,
                        colframe=black,
                        boxrule=0pt,
                        arc=6pt,
                        outer arc=6pt,
                        left=6pt,
                        right=6pt,
                        valign=center,
                        fontupper=\\footnotesize\\bfseries,
                    ]{\\textcolor{lighttextcolor}{#1}}
                }"""
            )
            .build();
    }
}
