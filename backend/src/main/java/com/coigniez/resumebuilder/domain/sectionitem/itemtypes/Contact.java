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
public class Contact implements SectionItemData {

    public static final int BASE_PARAMETER_COUNT = 3;

    public String icon;
    @NotBlank
    public String label;
    public String link;

    @JsonIgnore
    public List<String> getSectionItemData() {
        return List.of(
            Optional.ofNullable(icon).orElse(""),
            Optional.ofNullable(label).orElse(""),
            Optional.ofNullable(link).orElse("")
        );
    }
}
