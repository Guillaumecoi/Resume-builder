package com.coigniez.resumebuilder.templates;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.coigniez.resumebuilder.domain.column.dtos.CreateColumnRequest;
import com.coigniez.resumebuilder.domain.latex.dtos.CreateLatexMethodRequest;
import com.coigniez.resumebuilder.domain.layout.dtos.CreateLayoutRequest;
import com.coigniez.resumebuilder.domain.layout.enums.ColorLocation;
import com.coigniez.resumebuilder.templates.methods.LatexMethodTemplates;
public final class LayoutTemplate {

    private LayoutTemplate() {
    } // Prevent instantiation

    public static CreateLayoutRequest getDefaultSingleColumn() {
        return CreateLayoutRequest.builder()
                .numberOfColumns(1)
                .columns(getDefaultColumns(1))
                .colorScheme(ColorTemplates.EXECUTIVE_SUITE)
                .latexMethods(getStandardMethods())
                .build();
    }

    public static CreateLayoutRequest getDefaultTwoColumn() {
        return CreateLayoutRequest.builder()
                .numberOfColumns(2)
                .columns(getDefaultColumns(2))
                .colorScheme(ColorTemplates.EXECUTIVE_SUITE)
                .latexMethods(getStandardMethods())
                .build();
    }

    public static List<CreateColumnRequest> getDefaultColumns(int numberOfColumns) {
        List<CreateColumnRequest> columns = new ArrayList<>();
        columns.add(CreateColumnRequest.builder()
                .columnNumber(1)
                .backgroundColor(ColorLocation.DARK_BG)
                .textColor(ColorLocation.LIGHT_TEXT)
                .borderColor(ColorLocation.ACCENT)
                .build());
        if (numberOfColumns == 2) {
            columns.get(0).setBorderRight(2.4);
            columns.add(CreateColumnRequest.builder()
                    .columnNumber(2)
                    .backgroundColor(ColorLocation.LIGHT_BG)
                    .textColor(ColorLocation.DARK_TEXT)
                    .borderColor(ColorLocation.ACCENT)
                    .build());
        }
        return columns;
    }

    public static Set<CreateLatexMethodRequest> getStandardMethods() {
        HashSet<CreateLatexMethodRequest> result = new HashSet<>();
        result.add(LatexMethodTemplates.getContactTemplates().get("Standard Contact").toCreateLatexMethodRequest());
        result.add(LatexMethodTemplates.getTitleTemplates().get("Standard Title").toCreateLatexMethodRequest());
        result.add(LatexMethodTemplates.getEducationTemplates().get("Standard Education").toCreateLatexMethodRequest());
        result.add(LatexMethodTemplates.getExperienceTemplates().get("Standard Experience").toCreateLatexMethodRequest());
        result.add(LatexMethodTemplates.getSkillTemplates().get("Standard Skill").toCreateLatexMethodRequest());
        result.add(LatexMethodTemplates.getSkillTemplates().get("Skill Text").toCreateLatexMethodRequest());
        result.add(LatexMethodTemplates.getSkillTemplates().get("Skill Bullets").toCreateLatexMethodRequest());
        result.add(LatexMethodTemplates.getSkillTemplates().get("Skill Bar").toCreateLatexMethodRequest());
        result.add(LatexMethodTemplates.getSkillboxTemplates().get("Standard Skillbox").toCreateLatexMethodRequest());
        result.add(LatexMethodTemplates.getTextboxTemplates().get("Standard Textbox").toCreateLatexMethodRequest());
        result.add(LatexMethodTemplates.getPictureTemplates().get("Standard Picture").toCreateLatexMethodRequest());

        return result;
    }
}
