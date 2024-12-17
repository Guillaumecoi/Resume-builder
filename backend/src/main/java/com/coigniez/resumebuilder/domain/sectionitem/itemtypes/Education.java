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
public class Education implements SectionItemData {

    public static final int BASE_PARAMETER_COUNT = 4;

    @NotBlank
    private String degree;
    @NotBlank
    private String institution;
    private String period;
    private String description;

    @JsonIgnore
    public List<String> getSectionItemData() {
        return List.of(
            Optional.ofNullable(degree).orElse(""),
            Optional.ofNullable(institution).orElse(""),
            Optional.ofNullable(period).orElse(""),
            Optional.ofNullable(description).orElse("")
        );
    }
}

