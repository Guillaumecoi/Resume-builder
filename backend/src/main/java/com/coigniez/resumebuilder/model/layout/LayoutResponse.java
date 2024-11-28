package com.coigniez.resumebuilder.model.layout;

import lombok.Builder;
import lombok.Data;

@Data
@Builder 
public class LayoutResponse {
    private Long id;
    private String primaryColor;
    private String secondaryColor;
    private String accentColor;
    private String font;
    private int culombs;
}