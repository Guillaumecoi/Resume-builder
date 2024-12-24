package com.coigniez.resumebuilder.domain.latex;

import com.coigniez.resumebuilder.domain.columnsection.ColumnSection;
import com.coigniez.resumebuilder.domain.sectionitem.itemtypes.Contact;
import com.coigniez.resumebuilder.domain.sectionitem.itemtypes.Education;
import com.coigniez.resumebuilder.domain.sectionitem.itemtypes.Picture;
import com.coigniez.resumebuilder.domain.sectionitem.itemtypes.Skill;
import com.coigniez.resumebuilder.domain.sectionitem.itemtypes.Skillboxes;
import com.coigniez.resumebuilder.domain.sectionitem.itemtypes.Textbox;
import com.coigniez.resumebuilder.domain.sectionitem.itemtypes.Title;
import com.coigniez.resumebuilder.interfaces.LatexMethodProvider;
import com.coigniez.resumebuilder.domain.sectionitem.itemtypes.Experience;

public enum HasLatexMethod {
    SECTION(ColumnSection.class),
    TITLE(Title.class),
    EXPERIENCE(Experience.class),
    TEXTBOX(Textbox.class),
    SKILL(Skill.class),
    EDUCATION(Education.class),
    SKILL_BOXES(Skillboxes.class),
    PICTURE(Picture.class),
    CONTACT(Contact.class);

    private final Class<? extends LatexMethodProvider> dataType;

    HasLatexMethod(Class<? extends LatexMethodProvider> dataType) {
        this.dataType = dataType;
    }

    public Class<? extends LatexMethodProvider> getDataType() {
        return dataType;
    }

    public int getNumberOfParameters() {
        try {
            int baseParameterCount = (int) dataType.getMethod("getBaseParameterCount").invoke(null);
            if (baseParameterCount < 0) {
                throw new IllegalStateException("Base parameter count must be greater than or equal to 0");
            }
            return baseParameterCount;
        } catch (Exception e) {
            throw new RuntimeException("Failed to get base parameter count", e);
        }
    }

}
