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

    @NotBlank
    private String degree;
    @NotBlank
    private String institution;
    private String period;
    private String description;

    @Override
    @JsonIgnore
    public List<String> getData() {
        return List.of(
                degree,
                institution,
                Optional.ofNullable(period).orElse(""),
                Optional.ofNullable(description).orElse(""));
    }
}
