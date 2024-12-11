package com.coigniez.resumebuilder.latex;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coigniez.resumebuilder.domain.column.ColumnResponse;
import com.coigniez.resumebuilder.domain.columnsection.ColumnSectionResponse;
import com.coigniez.resumebuilder.domain.layout.LayoutResponse;
import com.coigniez.resumebuilder.domain.layout.enums.ColorLocation;
import com.coigniez.resumebuilder.domain.layout.enums.ColorScheme;
import com.coigniez.resumebuilder.domain.layout.enums.PageSize;
import com.coigniez.resumebuilder.domain.section.SectionResponse;
import com.coigniez.resumebuilder.domain.sectionitem.SectionItemData;
import com.coigniez.resumebuilder.domain.sectionitem.SectionItemMapper;
import com.coigniez.resumebuilder.domain.sectionitem.SectionItemResponse;
import com.coigniez.resumebuilder.domain.sectionitem.SectionItemType;
import com.coigniez.resumebuilder.domain.sectionitem.itemtypes.Contact;
import com.coigniez.resumebuilder.domain.sectionitem.itemtypes.Education;
import com.coigniez.resumebuilder.domain.sectionitem.itemtypes.Picture;
import com.coigniez.resumebuilder.domain.sectionitem.itemtypes.Skill;
import com.coigniez.resumebuilder.domain.sectionitem.itemtypes.Skillboxes;
import com.coigniez.resumebuilder.domain.sectionitem.itemtypes.Textbox;
import com.coigniez.resumebuilder.domain.sectionitem.itemtypes.Title;
import com.coigniez.resumebuilder.domain.sectionitem.itemtypes.WorkExperience;
import com.coigniez.resumebuilder.domain.sectionitem.itemtypes.Skill.SkillType;

@Service
public class LatexService {

    @Autowired
    private SectionItemMapper sectionItemMapper;
    
    public String generateLatexDocument(LayoutResponse layout) {
        StringBuilder latexcode = new StringBuilder();
        latexcode.append(getImports(layout.getPageSize(), layout.getColumnSeparator()) + "\n");
        latexcode.append(getColors(layout.getColorScheme()) + "\n");
        latexcode.append(layout.getLatexCommands().getAllMethods() + "\n");
        latexcode.append(getColumnColorboxMethods(layout.getColumns()) + "\n");
        
        latexcode.append("\\begin{document}\n");
        latexcode.append(getContent(layout));
        latexcode.append("\\end{document}\n");

        return latexcode.toString();
    }

    private String addTabToEachLine(String input, int tabCount) {
        return input.lines()
                    .map(line -> " ".repeat(tabCount * 4) + line)
                    .collect(Collectors.joining("\n"));
    }

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

    private String getColumnColorboxMethods(List<ColumnResponse> columns) {
        StringBuilder columnMethods = new StringBuilder();
        for (ColumnResponse column : columns) {
            String columnContent = String.format("\\newenvironment{tcolorbox%d}[0] { \n", column.getColumnNumber());

            columnContent += addTabToEachLine(
                    getTcolorbox(column.getBackgroundColor().toString(), 
                    column.getTextColor().toString(), 
                    column.getPaddingTop(), 
                    column.getPaddingBottom(), 
                    column.getPaddingLeft(), 
                    column.getPaddingRight()),
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

    private String getTcolorbox(String colback, String textcolor, Double top, Double bottom, Double left, Double right) {
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
                rightrule=0pt,
                colframe=%s
            ]
            \\color{%s}

            """, colback, left, right, top, bottom, ColorLocation.ACCENT.toString(), textcolor);
    }

    private String getContent(LayoutResponse layout) {
        StringBuilder content = new StringBuilder();
        content.append("\\begin{paracol}{%s}\n\n".formatted(layout.getNumberOfColumns()));

        for (ColumnResponse columnDto : layout.getColumns()) {
            content.append(getColumn(columnDto));
        }

        content.append("\\end{paracol}\n");
        return content.toString();
    }

    private String getColumn(ColumnResponse columnDto) {
        StringBuilder column = new StringBuilder();
        column.append("\\switchcolumn[%d]\n".formatted(columnDto.getColumnNumber() - 1));
        column.append("\\begin{tcolorbox%d}\n".formatted(columnDto.getColumnNumber()));
        for (ColumnSectionResponse columnSection : columnDto.getSectionMappings()) {
            column.append(addTabToEachLine(generateSection(columnSection), 1) + "\n");
        }
        column.append("\\end{tcolorbox%d}\n\n".formatted(columnDto.getColumnNumber()));

        return column.toString();
    }

    private String generateSection(ColumnSectionResponse columnSection) {
        SectionResponse section = columnSection.getSection();
        StringBuilder sectionString = new StringBuilder();

        String title = section.isShowTitle() ? section.getTitle() : "";

        // Start the section
        sectionString.append("\\begin{cvsection}{%s}{%.1fpt}{%.1fpt}{%s}\n"
                .formatted(title, columnSection.getItemsep(), columnSection.getEndsep(), ""));

        // Add the items
        for (SectionItemResponse item : section.getSectionItems()) {
            sectionString.append(addTabToEachLine(generateItem(item), 1) + "\n");
        }

        // End the section
        sectionString.append("\\end{cvsection}");

        return sectionString.toString();
    }

    private String generateItem(SectionItemResponse item) {
        SectionItemData object = sectionItemMapper.toDataObject(SectionItemType.valueOf(item.getType()), item.getData());

        if (object instanceof Title) {
            return generateTitle((Title) object);
        } else if (object instanceof Skill) {
            return generateSkill((Skill) object);
        } else if (object instanceof Education) {
            return generateEducation((Education) object);
        } else if (object instanceof Textbox) {
            return generateTextbox((Textbox) object);
        } else if (object instanceof WorkExperience) {
            return generateWorkexperience((WorkExperience) object);
        } else if (object instanceof Skillboxes) {
            return generateSkillboxes((Skillboxes) object);
        } else if (object instanceof Picture) {
            return generatePicture((Picture) object);
        } else if (object instanceof Contact) {
            return generateContact((Contact) object);
        } else {
            return "";
        }
    }

    private String generateTitle(Title title) {
        return "\\cvtitle{%s}{%s}".formatted(title.getTitle(), title.getSubtitle());
    }

    private String generateSkill(Skill skill) {
        if (skill.getType().equals(SkillType.SIMPLE)) {
            return "\\skillitem{%s}".formatted(skill.getName());
        } else if (skill.getType().equals(SkillType.TEXT)) {
            return "\\skilltext{%s}{%s}".formatted(skill.getName(), skill.getDescription());
        } else if (skill.getType().equals(SkillType.BULLETS)) {
            return "\\skillbullets{%s}{%d}".formatted(skill.getName(), skill.getProficiency());
        } else if (skill.getType().equals(SkillType.BAR)) {
            return "\\skillbar{%s}{%d}".formatted(skill.getName(), skill.getProficiency());
        } else {
            return "";
        }
    }

    private String generateSkillboxes(Skillboxes skillboxes) {
        StringBuilder skillboxesString = new StringBuilder();
        skillboxesString.append("\\begin{skillboxes}\n");

        for (String skill : skillboxes.getSkillsAsList()) {
            skillboxesString.append(
                    addTabToEachLine(
                        "\\skillbox{%s}".formatted(skill),
                         1)
                    + "\n");
        }

        skillboxesString.append("\\end{skillboxes}\n");

        return skillboxesString.toString();
    }

    private String generateEducation(Education education) {
        return """
                \\educationitem
                {%s}
                {%s}
                {%s}
                """.formatted(
                education.getDegree(), 
                education.getInstitution(), 
                education.getPeriod());
    }

    private String generateWorkexperience(WorkExperience workExperience) {
        String responsibilities = "\n";
        for (String responsibility : workExperience.getResponsibilitiesAsList()) {
            responsibilities += "\\item %s\n".formatted(responsibility);
        }

        return """
            \\experienceitem
            {%s}
            {%s}
            {%s}
            {%s}
            {%s}               
            """.formatted(
                workExperience.getCompanyName(),
                workExperience.getJobTitle(),
                workExperience.getPeriod(), 
                workExperience.getDescription(),
                responsibilities);
    }

    private String generateTextbox(Textbox textbox) {
        return "\\textbox{%s}".formatted(textbox.getContent());
    }

    private String generatePicture(Picture picture) {
        String profile = "\\pictureitem[%s]{%.2f}{%.2f}{%.2f}{%.2f}{%.2f}{%.2fpt}{%dpt}{%s}";

        return profile.formatted(
            picture.isCenter() ? "center" : "",
            picture.getWidth(),
            picture.getHeight(),
            picture.getXoffset(),
            picture.getYoffset(),
            picture.getZoom(),
            picture.getShadow(),
            picture.getRounded(),
            picture.getPath());         
    }

    private String generateContact(Contact contact) {
        return "\\contactitem{%s}{%s}{%s}".formatted(contact.getIcon(), contact.getLabel(), contact.getLink());
    }
}