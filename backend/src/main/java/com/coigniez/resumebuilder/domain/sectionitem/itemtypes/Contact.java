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

    @NotBlank
    public String label;
    public String icon;
    public String link;


    @Override
    @JsonIgnore
    public List<String> getData() {
        return List.of(
                Optional.ofNullable(label).orElse(""),
                Optional.ofNullable(icon).orElse(""),
                Optional.ofNullable(link).orElse(""));
    }
}
