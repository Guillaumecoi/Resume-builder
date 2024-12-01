package com.coigniez.resumebuilder.model.layout.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PageSize {
    A4("a4paper"),
    LETTER("letterpaper");

    private final String latexName;
}