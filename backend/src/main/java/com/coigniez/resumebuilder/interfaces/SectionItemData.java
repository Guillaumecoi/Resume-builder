package com.coigniez.resumebuilder.interfaces;

import org.springframework.validation.annotation.Validated;

import com.coigniez.resumebuilder.domain.sectionitem.itemtypes.Contact;
import com.coigniez.resumebuilder.domain.sectionitem.itemtypes.Education;
import com.coigniez.resumebuilder.domain.sectionitem.itemtypes.Picture;
import com.coigniez.resumebuilder.domain.sectionitem.itemtypes.Skill;
import com.coigniez.resumebuilder.domain.sectionitem.itemtypes.Skillboxes;
import com.coigniez.resumebuilder.domain.sectionitem.itemtypes.Textbox;
import com.coigniez.resumebuilder.domain.sectionitem.itemtypes.Title;
import com.coigniez.resumebuilder.domain.sectionitem.itemtypes.Experience;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Contact.class, name = "contact"),
        @JsonSubTypes.Type(value = Experience.class, name = "experience"),
        @JsonSubTypes.Type(value = Education.class, name = "education"),
        @JsonSubTypes.Type(value = Skill.class, name = "skill"),
        @JsonSubTypes.Type(value = Picture.class, name = "picture"),
        @JsonSubTypes.Type(value = Skillboxes.class, name = "skillboxes"),
        @JsonSubTypes.Type(value = Title.class, name = "title"),
        @JsonSubTypes.Type(value = Textbox.class, name = "textbox"),
})
@Validated
public interface SectionItemData extends LatexMethodProvider {

}
