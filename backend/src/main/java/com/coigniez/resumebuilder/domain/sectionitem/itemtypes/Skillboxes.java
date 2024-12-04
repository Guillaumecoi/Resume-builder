package com.coigniez.resumebuilder.domain.sectionitem.itemtypes;

import java.util.Arrays;
import java.util.List;

import com.coigniez.resumebuilder.domain.sectionitem.SectionItemData;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Skillboxes implements SectionItemData {
    private String skills; // Seperated by '/n'

    @JsonIgnore
    public List<String> getSkillsAsList() {
        return Arrays.asList(skills.split("\n"));
    }
}
