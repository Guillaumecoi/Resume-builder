package com.coigniez.resumebuilder.domain.sectionitem.itemtypes;

import com.coigniez.resumebuilder.interfaces.SectionItemData;

import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Skill implements SectionItemData {
    @NotBlank
    private String name;
    @Min(1)
    @Max(10)
    private Integer proficiency; 
    private String description;

    @NotNull
    @Enumerated
    @Builder.Default
    private SkillType type = SkillType.SIMPLE;

    public enum SkillType {
        SIMPLE,
        TEXT,       
        BULLETS,    
        BAR      
    }
}
