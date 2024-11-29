package com.coigniez.resumebuilder.model.layout.templates;

import com.coigniez.resumebuilder.model.layout.Layout;
import com.coigniez.resumebuilder.model.layout.enums.ColorScheme;
import com.coigniez.resumebuilder.model.layout.enums.LatexCommands;

public final class LayoutTemplate {
    
    private LayoutTemplate() {} // Prevent instantiation
    
    public static Layout getDefaultSingleColumn() {
        return Layout.builder()
                .numberOfColumns(1)
                .colorScheme(getExecutiveSuiteColors())
                .latexCommands(getBasicCommands())
                .build();
    }

    public static Layout getDefaultTwoColumn() {
        return Layout.builder()
                .numberOfColumns(2)
                .colorScheme(getExecutiveSuiteColors())
                .latexCommands(getBasicCommands())
                .build();
    }

    private static ColorScheme getExecutiveSuiteColors() {
        return ColorScheme.builder()
                .name("Executive Suite")
                .primary("#31323C") 
                .secondary("#dad7d5")      
                .accent("#613B2E")          
                .darkBg("#31323C")   
                .lightBg("#E8E8EA")  
                .darkText("#31323C")        
                .lightText("#dad7d5")     
                .build();
    }

       private static LatexCommands getBasicCommands() {
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
