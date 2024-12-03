package com.coigniez.resumebuilder.model.section.sectionitem.itemtypes;

import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkExperience implements SectionItemData {
    @NotBlank
    private String jobTitle;
    private String companyName;
    private String period;
    private String description;
    private String responsibilities;  // Items are seperated by the newline character '\n'

    @JsonIgnore
    public List<String> getResponsibilitiesAsList() {
        return Arrays.asList(responsibilities.split("\n"));
    }
}
