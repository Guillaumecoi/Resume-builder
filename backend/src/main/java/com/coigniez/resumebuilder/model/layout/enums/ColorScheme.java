package com.coigniez.resumebuilder.model.layout.enums;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
@Embeddable
public class ColorScheme {
    private String name;
    
    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$")
    private String primary;      

    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$")
    private String secondary;     
    
    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$")
    private String accent;       
    
    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$")
    private String darkBg;       
    
    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$")
    private String lightBg;      
    
    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$")
    private String darkText;     
    
    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$")
    private String lightText;    
}