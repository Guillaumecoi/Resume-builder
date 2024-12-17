package com.coigniez.resumebuilder.domain.latex;

import com.coigniez.resumebuilder.domain.sectionitem.SectionItemType;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LatexMethodResponse {
    
    private Long id;
    private SectionItemType type;
    private String name;
    private String method;
}