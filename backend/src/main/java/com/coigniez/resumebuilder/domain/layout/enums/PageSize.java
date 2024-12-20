package com.coigniez.resumebuilder.domain.layout.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PageSize {
    A4("a4paper"),
    A5("a5paper"),
    LETTER("letterpaper"),
    B5("b5paper"),
    EXECUTIVE("executivepaper");

    private final String latexName;
}
