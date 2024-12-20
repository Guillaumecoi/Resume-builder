package com.coigniez.resumebuilder.domain.layout.enums;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
@Embeddable
public class ColorScheme {
    @NotBlank
    private String name;
    
    @NotBlank
    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$")
    private String primaryColor;      

    @NotBlank
    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$")
    private String secondaryColor;     
    
    @NotBlank
    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$")
    private String accent;       
    
    @NotBlank
    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$")
    private String darkBg;       
    
    @NotBlank
    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$")
    private String lightBg;      
    
    @NotBlank
    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$")
    private String darkText;     
    
    @NotBlank
    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$")
    private String lightText;    
}