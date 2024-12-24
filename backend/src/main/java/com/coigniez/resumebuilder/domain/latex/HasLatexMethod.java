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
    SECTION(null, 3),
    SECTION_TITLE(ColumnSection.class, 2),
    TITLE(Title.class, 2),
    EXPERIENCE(Experience.class, 5),
    TEXTBOX(Textbox.class, 1),
    SKILL(Skill.class, 3),
    EDUCATION(Education.class, 4),
    SKILL_BOXES(Skillboxes.class, 1),
    PICTURE(Picture.class, 9),
    CONTACT(Contact.class, 3);

    private final Class<? extends LatexMethodProvider> dataType;
    private final int numberOfParameters;

    HasLatexMethod(Class<? extends LatexMethodProvider> dataType, int numberOfParameters) {
        this.dataType = dataType;
        this.numberOfParameters = numberOfParameters;
    }

    public Class<? extends LatexMethodProvider> getDataType() {
        return dataType;
    }

    public int getNumberOfParameters() {
        return numberOfParameters;
    }

}
