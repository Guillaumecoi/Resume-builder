package com.coigniez.resumebuilder.domain.resume.enums;

import lombok.Getter;

@Getter
public enum ResumeOrderBy {
    TITLE("title"),
    CREATED_DATE("createdDate"), 
    LAST_MODIFIED_DATE("lastModifiedDate");

    private final String fieldName;

    ResumeOrderBy(String fieldName) {
        this.fieldName = fieldName;
    }

    public static ResumeOrderBy fromString(String value) {
        return valueOf(value.toUpperCase());
    }
}