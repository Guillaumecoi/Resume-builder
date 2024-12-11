package com.coigniez.resumebuilder.latex.generators;

import org.springframework.stereotype.Component;

import com.coigniez.resumebuilder.domain.column.ColumnResponse;
import com.coigniez.resumebuilder.domain.columnsection.ColumnSectionResponse;
import com.coigniez.resumebuilder.domain.layout.LayoutResponse;
import com.coigniez.resumebuilder.domain.section.SectionResponse;
import com.coigniez.resumebuilder.domain.sectionitem.SectionItemData;
import com.coigniez.resumebuilder.domain.sectionitem.SectionItemMapper;
import com.coigniez.resumebuilder.domain.sectionitem.SectionItemResponse;
import com.coigniez.resumebuilder.domain.sectionitem.SectionItemType;
import com.coigniez.resumebuilder.domain.sectionitem.itemtypes.Contact;
import com.coigniez.resumebuilder.domain.sectionitem.itemtypes.Education;
import com.coigniez.resumebuilder.domain.sectionitem.itemtypes.Picture;
import com.coigniez.resumebuilder.domain.sectionitem.itemtypes.Skill;
import com.coigniez.resumebuilder.domain.sectionitem.itemtypes.Skill.SkillType;

import lombok.AllArgsConstructor;

import com.coigniez.resumebuilder.domain.sectionitem.itemtypes.Skillboxes;
import com.coigniez.resumebuilder.domain.sectionitem.itemtypes.Textbox;
import com.coigniez.resumebuilder.domain.sectionitem.itemtypes.Title;
import com.coigniez.resumebuilder.domain.sectionitem.itemtypes.WorkExperience;
import com.coigniez.resumebuilder.util.StringUtils;

@AllArgsConstructor
@Component
public class LatexContentGenerator implements LatexGenerator<LayoutResponse> {

    private final StringUtils stringUtils;
    private final SectionItemMapper sectionItemMapper;
    
    public String generate(LayoutResponse layout) {
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
            column.append(stringUtils.addTabToEachLine(generateSection(columnSection), 1) + "\n");
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
            sectionString.append(stringUtils.addTabToEachLine(generateItem(item), 1) + "\n");
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
                stringUtils.addTabToEachLine(
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