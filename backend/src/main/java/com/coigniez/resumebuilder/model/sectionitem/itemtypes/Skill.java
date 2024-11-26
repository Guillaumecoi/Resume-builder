package com.coigniez.resumebuilder.model.sectionitem.itemtypes;


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
public class Skill {
    @NotNull
    @NotBlank
    private String name;
    @Min(1)
    @Max(10)
    private Integer proficiency; 
}