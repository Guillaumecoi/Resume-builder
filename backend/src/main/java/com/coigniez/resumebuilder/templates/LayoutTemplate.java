package com.coigniez.resumebuilder.templates;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.coigniez.resumebuilder.domain.column.ColumnRequest;
import com.coigniez.resumebuilder.domain.latex.LatexMethodRequest;
import com.coigniez.resumebuilder.domain.layout.LayoutRequest;
import com.coigniez.resumebuilder.domain.layout.enums.ColorLocation;
import com.coigniez.resumebuilder.domain.layout.enums.ColorScheme;
import com.coigniez.resumebuilder.domain.sectionitem.SectionItemType;

public final class LayoutTemplate {
    
    private LayoutTemplate() {} // Prevent instantiation
    
    public static LayoutRequest getDefaultSingleColumn() {
        return LayoutRequest.builder()
            .numberOfColumns(1)
            .columns(getDefaultColumns(1))
            .colorScheme(getExecutiveSuiteColors())
            .latexMethods(getStandardMethods())
            .build();
}

    public static LayoutRequest getDefaultTwoColumn() {
        return LayoutRequest.builder()
            .numberOfColumns(2)
            .columns(getDefaultColumns(2))
            .colorScheme(getExecutiveSuiteColors())
            .latexMethods(getStandardMethods())
            .build();
    }

    public static List<ColumnRequest> getDefaultColumns(int numberOfColumns) {
        List<ColumnRequest> columns = new ArrayList<>();
        columns.add(ColumnRequest.builder()
                .columnNumber(1)
                .backgroundColor(ColorLocation.DARK_BG)
                .textColor(ColorLocation.LIGHT_TEXT)
                .borderColor(ColorLocation.ACCENT)
                .build());
        if (numberOfColumns == 2) {
            columns.get(0).setBorderRight(2.4);
            columns.add(ColumnRequest.builder()
                    .columnNumber(2)
                    .backgroundColor(ColorLocation.LIGHT_BG)
                    .textColor(ColorLocation.DARK_TEXT)
                    .borderColor(ColorLocation.ACCENT)
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

    public static Set<LatexMethodRequest> getStandardMethods() {
        HashSet<LatexMethodRequest> result = new HashSet<>();
        result.add(LatexMethodRequest.builder()
            .type(SectionItemType.TITLE)
            .name("cvtitle")
            .method("""
                {
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
            ).build());

        result.add(LatexMethodRequest.builder()
            .type(SectionItemType.CONTACT)
            .name("contactitem")
            .method("""
                {
                    \\ifthenelse{\\isempty{#1}}
                    {\\href{#3}{}}
                    {
                    \\begin{tabular}{@{}p{10pt} l@{}}
                        #1 & \\href{#3}{#2}
                    \\end{tabular}
                    }
                }"""
            ).build());

        result.add(LatexMethodRequest.builder()
            .type(SectionItemType.EDUCATION)
            .name("educationitem")
            .method("""
                {
                    \\textbf{#1} \\newline
                    \\textit{#2} \\newline
                    #3
                    \\ifthenelse{\\isempty{#4}}{}
                    {
                        \\footnotesize{#4}
                    }
                }"""
            ).build());

        result.add(LatexMethodRequest.builder()
            .type(SectionItemType.WORK_EXPERIENCE)
            .name("experienceitem")
            .method("""
                {
                    \\textbf{\\large#2} \\\\[4pt]
                    \\textbf{#1} \\hfill \\textit{#3} \\\\[-6pt]
                    \\textcolor{%s}{\\rule{\\linewidth}{0.4pt}} \\\\[2pt]
                    \\small#4
                    \\ifthenelse{\\isempty{#5}}{}
                    {
                        \\footnotesize{
                            \\begin{itemize}[left=5pt, itemsep=2pt, topsep=6pt]
                                #5
                            \\end{itemize}
                        }
                    }
                }""".formatted(ColorLocation.ACCENT.toString())
            ).build());

        result.add(LatexMethodRequest.builder()
            .type(SectionItemType.TEXTBOX)
            .name("textbox")
            .method("""
                {
                    \\small
                    #1
                }"""
            ).build());

        result.add(LatexMethodRequest.builder()
            .type(SectionItemType.SKILL)
            .name("skillitem")
            .method("""
                {
                    \\textbf{#1}
                }"""
            ).build());
        
        result.add(LatexMethodRequest.builder()
            .type(SectionItemType.SKILL)
            .name("skilltext")
            .method("""
                {
                    \\textbf{#1} \\hfill #2
                }"""
            ).build());

        result.add(LatexMethodRequest.builder()
            .type(SectionItemType.SKILL)
            .name("skillbullets")
            .method("""
                {
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
            ).build());
    
        result.add(LatexMethodRequest.builder()
            .type(SectionItemType.SKILL)
            .name("skillbar")
            .method("""
                {
                    \\textbf{#1} \\hfill
                    \\begin{tikzpicture}[baseline]
                        \\fill[%s,rounded corners=2pt] (0,0) rectangle (2,.15);
                        \\fill[%s,rounded corners=2pt] (0,0) rectangle ({2*#2/10},.15);
                    \\end{tikzpicture}
                }""".formatted(ColorLocation.LIGHT_TEXT.toString(), ColorLocation.ACCENT.toString())
            ).build());

        result.add(LatexMethodRequest.builder()
            .type(SectionItemType.SKILL_BOXES)
            .name("skillboxes")
            .method("""
                {
                    \\begin{minipage}[t]{\\textwidth}
                    \\setstretch{1.5}
                    \\raggedright
                    \\foreach \\skill in {#1} {
                        \\skillbox{\\skill}\\hspace{-8pt}
                    }
                    \\end{minipage}
                    \\setstretch{1}
                    \\vspace{4pt}
                }
                    
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
                }""".formatted(ColorLocation.ACCENT.toString(), ColorLocation.LIGHT_TEXT.toString())
            ).build());

        result.add(LatexMethodRequest.builder()
            .type(SectionItemType.PICTURE)
            .name("pictureitem")
            .method("""
                {
                    %% widthratio, heightratio, xoffset, yoffset, zoom, shadow, cornerradius, imageurl
                    \\begin{tikzpicture}
                        %% Set the Shadow
                        \\fill[rounded corners=#9, blur shadow={shadow xshift=#8, shadow yshift=-#8, shadow blur steps=10}] 
                            (0,0) rectangle (#3\\linewidth,#4\\linewidth);
                        %% Clip the image
                        \\clip[rounded corners=#9] (0,0) rectangle (#3\\linewidth,#4\\linewidth);
                        %% Insert the image
                        \\node[anchor=center, inner sep=0] (image) at 
                            ($(0.5*#3\\linewidth + #6,0.5*#4\\linewidth + #7)$) 
                            {\\includegraphics[width=#5\\linewidth]{#1}};
                    \\end{tikzpicture}
                    {\\footnotesize #2}
                }"""
            ).build());
        
        return result;
    }
}
