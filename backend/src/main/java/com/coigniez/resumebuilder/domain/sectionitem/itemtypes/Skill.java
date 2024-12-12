package com.coigniez.resumebuilder.domain.sectionitem.itemtypes;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.coigniez.resumebuilder.interfaces.SectionItemData;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Skill implements SectionItemData {

    public static final int BASE_PARAMETER_COUNT = 3;

    @NotBlank
    private String name;
    @Min(1)
    @Max(10)
    private Integer proficiency; 
    private String description;

    @JsonIgnore
    public List<String> getSectionItemData() {
        return List.of(
            name, 
            Objects.toString(proficiency, ""), 
            Optional.ofNullable(description).orElse("")
        );
    }
}
