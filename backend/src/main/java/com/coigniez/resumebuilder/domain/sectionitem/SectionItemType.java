package com.coigniez.resumebuilder.domain.sectionitem;

import com.coigniez.resumebuilder.domain.sectionitem.itemtypes.Contact;
import com.coigniez.resumebuilder.domain.sectionitem.itemtypes.Education;
import com.coigniez.resumebuilder.domain.sectionitem.itemtypes.Picture;
import com.coigniez.resumebuilder.domain.sectionitem.itemtypes.Skill;
import com.coigniez.resumebuilder.domain.sectionitem.itemtypes.Skillboxes;
import com.coigniez.resumebuilder.domain.sectionitem.itemtypes.Textbox;
import com.coigniez.resumebuilder.domain.sectionitem.itemtypes.Title;
import com.coigniez.resumebuilder.domain.sectionitem.itemtypes.WorkExperience;
import com.coigniez.resumebuilder.interfaces.SectionItemData;

public enum SectionItemType {
    TITLE(Title.class),
    WORK_EXPERIENCE(WorkExperience.class),
    TEXTBOX(Textbox.class),
    SKILL(Skill.class),
    EDUCATION(Education.class),
    SKILL_BOXES(Skillboxes.class),
    PICTURE(Picture.class),
    CONTACT(Contact.class);

    private final Class<? extends SectionItemData> dataType;

    SectionItemType(Class<? extends SectionItemData> dataType) {
        this.dataType = dataType;
    }

    public Class<? extends SectionItemData> getDataType() {
        return dataType;
    }

    public int getNumberOfParameters() {
        try {
            return dataType.getDeclaredField("BASE_PARAMETER_COUNT").getInt(null);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Could not access BASE_PARAMETER_COUNT in " + dataType.getSimpleName(), e);
        }
    }

}
