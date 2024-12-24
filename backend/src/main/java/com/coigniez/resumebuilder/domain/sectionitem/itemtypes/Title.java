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
public class Title implements SectionItemData {

    @NotBlank
    private String title;
    private String subtitle;

    @Override
    @JsonIgnore
    public List<String> getData() {
        return List.of(
                title,
                Optional.ofNullable(subtitle).orElse(""));
    }

}
