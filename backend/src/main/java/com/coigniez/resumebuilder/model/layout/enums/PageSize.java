package com.coigniez.resumebuilder.model.layout.enums;

public enum PageSize {
    A4("a4paper"),
    LETTER("letterpaper");

    private final String latexName;

    PageSize(String latexName) {
        this.latexName = latexName;
    }

    public String getLatexName() {
        return latexName;
    }
}
