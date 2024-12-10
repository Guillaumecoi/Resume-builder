package com.coigniez.resumebuilder.domain.sectionitem;

import com.coigniez.resumebuilder.domain.sectionitem.itemtypes.Education;
import com.coigniez.resumebuilder.domain.sectionitem.itemtypes.Picture;
import com.coigniez.resumebuilder.domain.sectionitem.itemtypes.Skill;
import com.coigniez.resumebuilder.domain.sectionitem.itemtypes.Skillboxes;
import com.coigniez.resumebuilder.domain.sectionitem.itemtypes.Textbox;
import com.coigniez.resumebuilder.domain.sectionitem.itemtypes.Title;
import com.coigniez.resumebuilder.domain.sectionitem.itemtypes.WorkExperience;

public enum SectionItemType {
    TITLE(Title.class),
    WORK_EXPERIENCE(WorkExperience.class),
    TEXTBOX(Textbox.class),
    SKILL(Skill.class),
    EDUCATION(Education.class),
    SKILL_BOXES(Skillboxes.class),
    PICTURE(Picture.class);

    private final Class<?> dataType;

    SectionItemType(Class<?> dataType) {
        this.dataType = dataType;
    }

    public Class<?> getDataType() {
        return dataType;
    }

    /**
     * Converts a string to SectionItemType, handling nulls and invalid values.
     * @param typeName the name to convert
     * @return the corresponding SectionItemType
     * @throws IllegalArgumentException if the typeName is invalid
     */
    public static SectionItemType fromString(String typeName) {
        if (typeName == null || typeName.isBlank()) {
            throw new IllegalArgumentException("SectionItemType cannot be null or blank");
        }
        try {
            return SectionItemType.valueOf(typeName.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid SectionItemType: " + typeName, e);
        }
    }
}
