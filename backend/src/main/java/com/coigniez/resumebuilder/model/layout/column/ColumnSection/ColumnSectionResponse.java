package com.coigniez.resumebuilder.model.layout.column.ColumnSection;

import com.coigniez.resumebuilder.model.section.SectionResponse;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ColumnSectionResponse {

    private Long id;
    private SectionResponse section;
    private Integer position;
    private double itemsep;
}