package com.coigniez.resumebuilder.domain.sectionitem;

import java.util.Map;

import com.coigniez.resumebuilder.domain.layout.enums.AlignmentType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SectionItemRequest{

    private Long id;
    private long sectionId;
    @NotNull
    private long latexMethodId;
    @NotBlank
    private String type;
    private Integer itemOrder;
    private AlignmentType alignment;

    @NotBlank
    private Map<String, Object> data;

}