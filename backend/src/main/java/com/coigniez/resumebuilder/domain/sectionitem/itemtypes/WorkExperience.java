package com.coigniez.resumebuilder.domain.sectionitem.itemtypes;

import java.util.List;
import java.util.Optional;

import com.coigniez.resumebuilder.interfaces.SectionItemData;
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

    public static final int BASE_PARAMETER_COUNT = 5;

    @NotBlank
    private String jobTitle;
    private String companyName;
    private String period;
    private String description;
    private List<String> responsibilities;

    @JsonIgnore
    public String getResponsibilitiesAsItems() {
        if (responsibilities == null) {
            return "";
        }
        return responsibilities.stream()
                     .map(r -> "\\item " + r.trim())
                     .reduce((a, b) -> a + "\n" + b)
                     .orElse("");
    }

    @JsonIgnore
    public List<String> getSectionItemData() {
        return List.of(
            jobTitle, 
            Optional.ofNullable(companyName).orElse(""),
            Optional.ofNullable(period).orElse(""),
            Optional.ofNullable(description).orElse(""),
            getResponsibilitiesAsItems());
    }
}
