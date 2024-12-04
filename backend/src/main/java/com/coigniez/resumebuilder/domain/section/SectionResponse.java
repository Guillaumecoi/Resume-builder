package com.coigniez.resumebuilder.domain.section;

import java.util.List;

import com.coigniez.resumebuilder.domain.sectionitem.SectionItemResponse;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SectionResponse {

    private Long id;
    private String title;
    private List<SectionItemResponse> sectionItems;
    
}
