package com.coigniez.resumebuilder.domain.layout.templates;

import java.util.ArrayList;
import java.util.List;

import com.coigniez.resumebuilder.domain.column.ColumnRequest;
import com.coigniez.resumebuilder.domain.layout.LayoutRequest;
import com.coigniez.resumebuilder.domain.layout.enums.ColorLocation;
import com.coigniez.resumebuilder.domain.layout.enums.ColorScheme;
import com.coigniez.resumebuilder.domain.layout.enums.LatexCommands;

public final class LayoutTemplate {
    
    private LayoutTemplate() {} // Prevent instantiation
    
    public static LayoutRequest getDefaultSingleColumn() {
        return LayoutRequest.builder()
            .numberOfColumns(1)
            .columns(getDefaultColumns(1))
            .colorScheme(getExecutiveSuiteColors())
            .latexCommands(getBasicCommands())
            .build();
}

    public static LayoutRequest getDefaultTwoColumn() {
        return LayoutRequest.builder()
            .numberOfColumns(2)
            .columns(getDefaultColumns(2))
            .colorScheme(getExecutiveSuiteColors())
            .latexCommands(getBasicCommands())
            .build();
    }

    public static List<ColumnRequest> getDefaultColumns(int numberOfColumns) {
        List<ColumnRequest> columns = new ArrayList<>();
        columns.add(ColumnRequest.builder()
                .columnNumber(1)
                .backgroundColor(ColorLocation.DARK_BG)
                .textColor(ColorLocation.LIGHT_TEXT)
                .build());
        if (numberOfColumns == 2) {
            columns.add(ColumnRequest.builder()
                    .columnNumber(2)
                    .backgroundColor(ColorLocation.LIGHT_BG)
                    .textColor(ColorLocation.DARK_TEXT)
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
            .sectionTitle("""
                \\newcommand{\\sectiontitle}[1] {
                    \\hspace{6pt}\\textbf{\\Large{\\uppercase{#1}}}\\\\[-4pt]
                    \\textcolor{%s}{\\rule{80pt}{2pt}}
                }""".formatted(ColorLocation.ACCENT.toString())
            )
            .title("""
                \\newcommand{\\cvtitle}[2]{
                    \\item
                    \\begin{minipage}[t]{\\textwidth}
                        \\hspace{6pt}\\textbf{\\Huge \\textcolor{%s}{#1}}
                        \\ifthenelse{\\isempty{#2}}
                        {}
                        {
                            \\par\\vspace{6pt}
                            \\hspace{6pt}\\textbf{\\Large #2}
                        }
                    \\end{minipage}
                }""".formatted(ColorLocation.ACCENT.toString())
            )
            .contactItem("""
                \\newcommand{\\contactitem}[3]{
                    \\item
                    \\ifthenelse{\\isempty{#1}}
                    {\\href{#3}{}}
                    {
                    \\begin{tabular}{@{}p{10pt} l@{}}
                        #1 & \\href{#3}{#2}
                    \\end{tabular}
                    }
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
                    \\textbf{\\large#2} \\\\[4pt]
                    \\textbf{#1} \\hfill \\textit{#3} \\\\[-6pt]
                    \\textcolor{%s}{\\rule{\\linewidth}{0.4pt}} \\\\[2pt]
                    \\small#4
                    \\ifthenelse{\\isempty{#5}}{}{
                        \\footnotesize{
                            \\begin{itemize}[left=5pt, itemsep=2pt, topsep=6pt]
                                #5
                            \\end{itemize}
                        }
                    }
                }""".formatted(ColorLocation.ACCENT.toString())
            )
            .textbox("""
                \\newcommand{\\textbox}[1]{
                    \\item \\small
                    #1
                }"""
            )
            .skillitem("""
                \\newcommand{\\skillitem}[1]{
                    \\item
                    \\textbf{#1}
                }"""
            )
            .skilltext("""
                \\newcommand{\\skilltext}[2]{
                    \\item
                    \\textbf{#1} \\hfill #2
                }"""
            )
            .skillbullets("""
                \\newcommand{\\skillbullets}[2]{
                    \\item
                    \\textbf{#1} \\hfill
                    \\foreach \\x in {1,2,3,4,5}{
                        \\pgfmathparse{#2/2>=\\x}
                        \\ifnum\\pgfmathresult=1
                            \\textcolor{%s}{$\\bullet$}
                        \\else
                            \\textcolor{%s}{$\\circ$}
                        \\fi
                    }
                }""".formatted(ColorLocation.ACCENT.toString(), ColorLocation.LIGHT_TEXT.toString())
            )
            .skillbar("""
                \\newcommand{\\skillbar}[2]{
                    \\item
                    \\textbf{#1} \\hfill
                    \\begin{tikzpicture}[baseline]
                        \\fill[%s,rounded corners=2pt] (0,0) rectangle (2,.15);
                        \\fill[%s,rounded corners=2pt] (0,0) rectangle ({2*#2/10},.15);
                    \\end{tikzpicture}
                }""".formatted(ColorLocation.LIGHT_TEXT.toString(), ColorLocation.ACCENT.toString())
            )
            .skillbox("""
                \\newcommand{\\skillbox}[1]{
                    \\tcbox[
                        enhanced,
                        nobeforeafter,
                        size=minimal,
                        height=1.4em,
                        colback=%s,
                        colframe=black,
                        boxrule=0pt,
                        arc=6pt,
                        outer arc=6pt,
                        left=6pt,
                        right=6pt,
                        valign=center,
                        fontupper=\\footnotesize\\bfseries,
                    ]{\\textcolor{%s}{#1}}
                    \\vspace{2pt}
                }""".formatted(ColorLocation.ACCENT.toString(), ColorLocation.LIGHT_TEXT.toString())
            )
            .skillboxes("""
                \\newenvironment{skillboxes}[0]{
                    \\item
                    \\begin{minipage}[t]{\\textwidth}
                    \\raggedright
                        }{
                    \\end{minipage}
                }"""
            )
            .picture("""
                    %% [center], widthratio, heightratio, xoffset, yoffset, zoom, shadow, cornerradius, imageurl
                    \\newcommand{\\pictureitem}[9][]{
                        \\item
                        \\ifthenelse{\\equal{#1}{center}}{
                            \\begin{center}
                        }{}
                        \\begin{tikzpicture}
                            %% Set the Shadow
                            \\fill[rounded corners=#8, blur shadow={shadow xshift=#7, shadow yshift=-#7, shadow blur steps=10}] 
			                    (0,0) rectangle (#2\\linewidth,#3\\linewidth);
                            %% Clip the image
                            \\clip[rounded corners=#8] (0,0) rectangle (#2\\linewidth,#3\\linewidth);
                            %% Insert the image
                            \\node[anchor=center, inner sep=0] (image) at 
                                ($(0.5*#2\\linewidth + #4,0.5*#3\\linewidth + #5)$) 
                                {\\includegraphics[width=#6\\linewidth]{#9}};
                        \\end{tikzpicture}
                        \\ifthenelse{\\equal{#1}{center}}{
                            \\end{center}
                        }{}
                    }"""
            )
            .build();
    }
}
