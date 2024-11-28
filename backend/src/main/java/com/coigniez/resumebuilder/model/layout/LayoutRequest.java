package com.coigniez.resumebuilder.model.layout;

public record LayoutRequest(
    String primaryColor,
    String secondaryColor,
    String accentColor,
    String font,
    int culombs
) {}