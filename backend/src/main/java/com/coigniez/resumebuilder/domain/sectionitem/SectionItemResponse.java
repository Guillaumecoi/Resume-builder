package com.coigniez.resumebuilder.domain.sectionitem;

import java.util.Map;

import com.coigniez.resumebuilder.domain.latex.LatexMethodResponse;
import com.coigniez.resumebuilder.domain.layout.enums.AlignmentType;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SectionItemResponse {
    private long id;
    private LatexMethodResponse latexMethod;
    private String type;
    private int itemOrder;
    private AlignmentType alignment;
    private Map<String, Object> data;
}