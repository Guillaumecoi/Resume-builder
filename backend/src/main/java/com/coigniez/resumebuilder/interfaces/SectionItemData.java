package com.coigniez.resumebuilder.interfaces;

import java.util.List;

import org.springframework.validation.annotation.Validated;

import com.coigniez.resumebuilder.domain.sectionitem.itemtypes.Contact;
import com.coigniez.resumebuilder.domain.sectionitem.itemtypes.Education;
import com.coigniez.resumebuilder.domain.sectionitem.itemtypes.Picture;
import com.coigniez.resumebuilder.domain.sectionitem.itemtypes.Skill;
import com.coigniez.resumebuilder.domain.sectionitem.itemtypes.Skillboxes;
import com.coigniez.resumebuilder.domain.sectionitem.itemtypes.Textbox;
import com.coigniez.resumebuilder.domain.sectionitem.itemtypes.Title;
import com.coigniez.resumebuilder.domain.sectionitem.itemtypes.WorkExperience;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
@JsonSubTypes({
    @JsonSubTypes.Type(value = Contact.class, name = "contact"),
    @JsonSubTypes.Type(value = WorkExperience.class, name = "experience"),
    @JsonSubTypes.Type(value = Education.class, name = "education"),
    @JsonSubTypes.Type(value = Skill.class, name = "skill"),
    @JsonSubTypes.Type(value = Picture.class, name = "picture"),
    @JsonSubTypes.Type(value = Skillboxes.class, name = "skillboxes"),
    @JsonSubTypes.Type(value = Title.class, name = "title"),
    @JsonSubTypes.Type(value = Textbox.class, name = "textbox"),
})
@Validated
public interface SectionItemData {

    /**
     * The number of parameters getSectionItemData() should return.
     * This value needs to be overwritten in the implementing class.
     */
    int BASE_PARAMETER_COUNT = 0;

    /**
     * Provides the list of data fields required for generating LaTeX commands.
     * Each item in the list corresponds to a placeholder in the command.
     * 
     * @return List of strings representing the data for LaTeX generation
     */
    public List<String> getSectionItemData();
    
}
