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

    public static final int BASE_PARAMETER_COUNT = 2;

    @NotBlank
    private String title;
    private String subtitle;
    
    @JsonIgnore
    public List<String> getSectionItemData() {
        return List.of(
            title, 
            Optional.ofNullable(subtitle).orElse("")
        );
    }

    @JsonIgnore
    public static int getNumberOfParameters() {
        return 2;
    }
    
}
