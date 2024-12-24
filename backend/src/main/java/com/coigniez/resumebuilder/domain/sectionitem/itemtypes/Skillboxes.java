package com.coigniez.resumebuilder.domain.sectionitem.itemtypes;

import java.util.List;

import com.coigniez.resumebuilder.interfaces.SectionItemData;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Skillboxes implements SectionItemData {

    @NotNull
    @NotEmpty
    private List<String> skills;

    @Override
    @JsonIgnore
    public List<String> getData() {
        return List.of(skills.toString());
    }
}
