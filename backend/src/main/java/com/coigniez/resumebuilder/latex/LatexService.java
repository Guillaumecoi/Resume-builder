package com.coigniez.resumebuilder.latex;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.coigniez.resumebuilder.model.layout.LayoutResponse;
import com.coigniez.resumebuilder.model.layout.column.ColumnResponse;
import com.coigniez.resumebuilder.model.layout.column.ColumnSection.ColumnSectionResponse;
import com.coigniez.resumebuilder.model.layout.enums.ColorLocation;
import com.coigniez.resumebuilder.model.layout.enums.ColorScheme;
import com.coigniez.resumebuilder.model.layout.enums.PageSize;
import com.coigniez.resumebuilder.model.section.SectionResponse;
import com.coigniez.resumebuilder.model.section.sectionitem.SectionItemMapper;
import com.coigniez.resumebuilder.model.section.sectionitem.SectionItemResponse;
import com.coigniez.resumebuilder.model.section.sectionitem.itemtypes.Education;
import com.coigniez.resumebuilder.model.section.sectionitem.itemtypes.SectionItemData;
import com.coigniez.resumebuilder.model.section.sectionitem.itemtypes.SectionItemType;
import com.coigniez.resumebuilder.model.section.sectionitem.itemtypes.Skill;
import com.coigniez.resumebuilder.model.section.sectionitem.itemtypes.Textbox;
import com.coigniez.resumebuilder.model.section.sectionitem.itemtypes.WorkExperience;
import com.coigniez.resumebuilder.model.section.sectionitem.itemtypes.Skill.SkillType;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class LatexService {

    private final SectionItemMapper sectionItemMapper;
    
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

            \\columnratio{%.1f}
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

        // Start the section
        sectionString.append("\\begin{cvsection}{%s}{%.1fpt}{%s}\n"
                .formatted(section.getTitle(), columnSection.getItemsep(), ""));

        Boolean LastItemWasSkillBox = false;
        // Add the items
        for (SectionItemResponse item : section.getSectionItems()) {
            sectionString.append(addTabToEachLine(generateItem(item, LastItemWasSkillBox), 1) + "\n");
        }

        // End the section
        sectionString.append("\\end{cvsection}");

        return sectionString.toString();
    }

    private String generateItem(SectionItemResponse item, boolean LastItemWasSkillBox) {
        SectionItemData object = sectionItemMapper.toDataObject(SectionItemType.valueOf(item.getType()), item.getData());

        if (object instanceof Skill) {
            return generateSkill((Skill) object, LastItemWasSkillBox);
        } else if (object instanceof Education) {
            return generateEducation((Education) object);
        } else if (object instanceof Textbox) {
            return generateTextbox((Textbox) object);
        } else if (object instanceof WorkExperience) {
            return generateWorkexperience((WorkExperience) object);
        } else {
            return "";
        }
    }

    private String generateSkill(Skill skill, boolean LastItemWasSkillBox) {
        if (skill.getType().equals(SkillType.BOX)) {
            // The set of skillboxed are an item instead of each skill being an item
            String skillbox = LastItemWasSkillBox ? "\\item \\setstretch \\rightrigged\n" : "";
            skillbox += "\\skillbox{%s}".formatted(skill.getName());
            return skillbox;
        } else if (skill.getType().equals(SkillType.SIMPLE)) {
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
}