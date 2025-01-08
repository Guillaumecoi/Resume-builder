package com.coigniez.resumebuilder.domain.layout.embedded;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
@Embeddable
public class ColorScheme {
    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String name;
    
    @NotBlank
    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$")
    @Column(nullable = false, length = 7)
    private String primaryColor;      

    @NotBlank
    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$")
    @Column(nullable = false, length = 7)
    private String secondaryColor;     
    
    @NotBlank
    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$")
    @Column(nullable = false, length = 7)
    private String accent;       
    
    @NotBlank
    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$")
    @Column(nullable = false, length = 7)
    private String darkBg;       
    
    @NotBlank
    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$")
    @Column(nullable = false, length = 7)
    private String lightBg;      
    
    @NotBlank
    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$")
    @Column(nullable = false, length = 7)
    private String darkText;     
    
    @NotBlank
    @Pattern(regexp = "^#[0-9A-Fa-f]{6}$")
    @Column(nullable = false, length = 7)
    private String lightText;    
}