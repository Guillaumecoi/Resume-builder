package com.coigniez.resumebuilder.interfaces;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
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
