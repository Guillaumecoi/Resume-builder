package com.coigniez.resumebuilder.templates.methods;

import java.util.HashMap;
import java.util.Map;

import com.coigniez.resumebuilder.domain.latex.HasLatexMethod;
import com.coigniez.resumebuilder.domain.latex.MethodType;
import com.coigniez.resumebuilder.domain.layout.enums.ColorLocation;

public class LatexMethodTemplates {

    private LatexMethodTemplates() {
    } // Prevent instantiation

    public static Map<String, LatexMethodTemplate> getTemplates() {
        Map<String, LatexMethodTemplate> templates = new HashMap<>();

        templates.putAll(getSectionTitlesTemplates());
        templates.putAll(getContactTemplates());
        templates.putAll(getTitleTemplates());
        templates.putAll(getEducationTemplates());
        templates.putAll(getExperienceTemplates());
        templates.putAll(getSkillTemplates());
        templates.putAll(getSkillboxTemplates());
        templates.putAll(getTextboxTemplates());

        return templates;
    }

    public static LatexMethodTemplate getSectionTemplate() {

        return new LatexMethodTemplate(
                HasLatexMethod.SECTION,
                "cvsection",
                MethodType.ENVIRONMENT,
                """
                        {
                            \\def\\cvsectionvspace{#3}
                            #1
                            \\begin{itemize}[left=0pt, itemsep=#2, label={}, topsep=10pt]
                                }{
                            \\end{itemize}
                            \\vspace{\\cvsectionvspace}
                        }
                        """,
                "Standard Section",
                null);
    }

    public static Map<String, LatexMethodTemplate> getSectionTitlesTemplates() {
        Map<String, LatexMethodTemplate> contactTemplates = new HashMap<>();

        contactTemplates.put("Standard Section Title", new LatexMethodTemplate(
                HasLatexMethod.SECTION_TITLE,
                "sectiontitle",
                MethodType.COMMAND,
                """
                        {
                            \\ifthenelse{\\equal{#1}{}}{}{
                                \\hspace{6pt}\\textbf{\\Large{\\uppercase{#1}}}\\\\[-4pt]
                                \\textcolor{%s}{\\rule{80pt}{2pt}}
                            }
                        }
                        """.formatted(ColorLocation.ACCENT.toString()),
                "Standard Section Title",
                null));

        return contactTemplates;
    }

    public static Map<String, LatexMethodTemplate> getContactTemplates() {
        Map<String, LatexMethodTemplate> contactTemplates = new HashMap<>();

        contactTemplates.put("Standard Contact", new LatexMethodTemplate(
                HasLatexMethod.CONTACT,
                "contactitem",
                MethodType.COMMAND,
                """
                        {
                            \\ifthenelse{\\isempty{#2}}
                            {\\href{#3}{#1}}
                            {
                            \\begin{tabular}{@{}p{10pt} l@{}}
                                #2 & \\href{#3}{#1}
                            \\end{tabular}
                            }
                        }""",
                "Standard Contact",
                null));

        return contactTemplates;
    }

    public static Map<String, LatexMethodTemplate> getTitleTemplates() {
        Map<String, LatexMethodTemplate> titleTemplates = new HashMap<>();

        titleTemplates.put("Standard Title", new LatexMethodTemplate(
                HasLatexMethod.TITLE,
                "cvtitle",
                MethodType.COMMAND,
                """
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
                        }""".formatted(ColorLocation.PRIMARY.toString()),
                "Standard Title",
                null));

        return titleTemplates;
    }

    public static Map<String, LatexMethodTemplate> getEducationTemplates() {
        Map<String, LatexMethodTemplate> educationTemplates = new HashMap<>();

        educationTemplates.put("Standard Education", new LatexMethodTemplate(
                HasLatexMethod.EDUCATION,
                "educationitem",
                MethodType.COMMAND,
                """
                        {
                            \\textbf{#1} \\newline
                            \\textit{#2} \\newline
                            #3
                            \\ifthenelse{\\isempty{#4}}{}
                            {
                                \\footnotesize{#4}
                            }
                        }""",
                "Standard Education",
                null));

        return educationTemplates;
    }

    public static Map<String, LatexMethodTemplate> getExperienceTemplates() {
        Map<String, LatexMethodTemplate> experienceTemplateMap = new HashMap<>();

        experienceTemplateMap.put("Standard Experience", new LatexMethodTemplate(
                HasLatexMethod.EXPERIENCE,
                "experienceitem",
                MethodType.COMMAND,
                """
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
                        }""".formatted(ColorLocation.ACCENT.toString()),
                "Standard Experience",
                null));

        return experienceTemplateMap;
    }

    public static Map<String, LatexMethodTemplate> getSkillTemplates() {
        Map<String, LatexMethodTemplate> skillTemplates = new HashMap<>();

        skillTemplates.put("Standard Skill", new LatexMethodTemplate(
                HasLatexMethod.SKILL,
                "skillitem",
                MethodType.COMMAND,
                """
                        {
                            \\textbf{#1}
                        }""",
                "Skill Item",
                null));

        skillTemplates.put("Skill Text", new LatexMethodTemplate(
                HasLatexMethod.SKILL,
                "skilltext",
                MethodType.COMMAND,
                """
                        {
                            \\textbf{#1} \\hfill #2
                        }""",
                "Skill Text",
                null));

        skillTemplates.put("Skill Bullets", new LatexMethodTemplate(
                HasLatexMethod.SKILL,
                "skillbullets",
                MethodType.COMMAND,
                """
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
                        }""".formatted(ColorLocation.ACCENT.toString(), ColorLocation.LIGHT_TEXT.toString()),
                "Skill Bullets",
                null));

        skillTemplates.put("Skill Bar", new LatexMethodTemplate(
                HasLatexMethod.SKILL,
                "skillbar",
                MethodType.COMMAND,
                """
                        {
                            \\textbf{#1} \\hfill
                            \\begin{tikzpicture}[baseline]
                                \\fill[%s,rounded corners=2pt] (0,0) rectangle (2,.15);
                                \\fill[%s,rounded corners=2pt] (0,0) rectangle ({2*#2/10},.15);
                            \\end{tikzpicture}
                        }""".formatted(ColorLocation.LIGHT_TEXT.toString(), ColorLocation.ACCENT.toString()),
                "Skill Bar",
                null));

        return skillTemplates;
    }

    public static Map<String, LatexMethodTemplate> getSkillboxTemplates() {
        Map<String, LatexMethodTemplate> skillboxTemplates = new HashMap<>();

        skillboxTemplates.put("Standard Skillbox", new LatexMethodTemplate(
                HasLatexMethod.SKILL_BOXES,
                "skillboxes",
                MethodType.COMMAND,
                """
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
                        }""".formatted(ColorLocation.ACCENT.toString(), ColorLocation.LIGHT_TEXT.toString()),
                "Skillbox",
                null));

        return skillboxTemplates;
    }

    public static Map<String, LatexMethodTemplate> getTextboxTemplates() {
        Map<String, LatexMethodTemplate> textboxTemplates = new HashMap<>();

        textboxTemplates.put("Standard Textbox", new LatexMethodTemplate(
                HasLatexMethod.TEXTBOX,
                "textbox",
                MethodType.COMMAND,
                """
                        {
                            \\small
                            #1
                        }""",
                "Standard Textbox",
                null));

        return textboxTemplates;
    }

    public static Map<String, LatexMethodTemplate> getPictureTemplates() {
        Map<String, LatexMethodTemplate> pictureTemplates = new HashMap<>();

        pictureTemplates.put("Standard Picture", new LatexMethodTemplate(
                HasLatexMethod.PICTURE,
                "pictureitem",
                MethodType.COMMAND,
                """
                        {
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
                        }""",
                "Standard Picture",
                null));

        return pictureTemplates;
    }

}
