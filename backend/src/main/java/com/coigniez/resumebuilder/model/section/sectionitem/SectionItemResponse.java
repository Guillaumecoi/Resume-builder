package com.coigniez.resumebuilder.model.section.sectionitem;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SectionItemResponse {
    private Long id;
    private String type;
    private Integer itemOrder;
    private Map<String, Object> data;
}