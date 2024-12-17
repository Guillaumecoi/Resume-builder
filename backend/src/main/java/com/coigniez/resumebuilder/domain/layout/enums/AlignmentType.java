package com.coigniez.resumebuilder.domain.layout.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AlignmentType {
    LEFT("flushleft"),
    CENTER("center"),
    RIGHT("flushright"),
    JUSTIFIED("justify");

    private final String latexCommand;
}
