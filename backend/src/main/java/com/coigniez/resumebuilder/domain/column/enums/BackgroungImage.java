package com.coigniez.resumebuilder.domain.column.enums;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class BackgroungImage {

    @NotBlank
    private String imagePath;
    @Builder.Default
    @Min(0)
    @Max(1)
    @Column(columnDefinition = "DECIMAL(3,2)")
    private Float opacity = 1.0f;
    @Builder.Default
    private Boolean keepAspectRatioByHeight = true;

    @JsonIgnore
    public List<String> getData() {
        String scaling;
        if (keepAspectRatioByHeight) {
            scaling = "keepaspectratio, height=\\textheight";
        } else if (!keepAspectRatioByHeight) {
            scaling = "keepaspectratio, width=\\textwidth";
        } else {
            scaling = "";
        }
        return List.of(imagePath, String.valueOf(opacity), scaling);
    }
    
}