package com.coigniez.resumebuilder.domain.sectionitem.itemtypes;

import java.util.List;

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
public class Skillboxes implements SectionItemData {
    
    public static final int BASE_PARAMETER_COUNT = 1;
    
    @NotBlank
    private List<String> skills;

    @JsonIgnore
    public List<String> getSectionItemData() {
        return skills;
    }
}

