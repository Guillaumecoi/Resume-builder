package com.coigniez.resumebuilder.domain.column.enums;

import org.hibernate.annotations.ColumnDefault;

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
    @Min(0)
    @Max(1)
    @Column(columnDefinition = "DECIMAL(3,2)")
    @ColumnDefault("1.0")
    private Float opacity;
    @ColumnDefault("true")
    private Boolean keepAspectRatioByHeight;
    
}