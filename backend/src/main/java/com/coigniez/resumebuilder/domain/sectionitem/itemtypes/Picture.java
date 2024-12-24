package com.coigniez.resumebuilder.domain.sectionitem.itemtypes;

import java.util.List;
import java.util.Optional;

import com.coigniez.resumebuilder.interfaces.SectionItemData;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Picture implements SectionItemData {

    @NotBlank
    private String path;  
    private String caption;

    @Builder.Default
    @DecimalMin("0.05") @DecimalMax("1.0")
    private double width = 0.9;
    @Builder.Default
    private double height = 1.1;

    @Builder.Default
    private int rounded = 60;

    @Builder.Default
    private double zoom = 1.0;

    @Builder.Default
    private double xoffset = 0.0;
    @Builder.Default
    private double yoffset = 0.0;

    @Builder.Default
    @Min(0) @Max(5)
    private double shadow = 0.0;
    
    @Override
    @JsonIgnore
    public List<String> getData() {
        return List.of(
            path,
            Optional.ofNullable(caption).orElse(""),
            String.format("%.2f", width),
            String.format("%.2f", height), 
            String.format("%.1f", zoom),
            String.format("%.1f", xoffset),
            String.format("%.1f", yoffset),
            String.format("%.1f", shadow),
            Integer.toString(rounded)
        );
    }
}