package com.coigniez.resumebuilder.model.section;

import java.util.List;

import com.coigniez.resumebuilder.model.sectionitem.SectionItemResponse;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SectionResponse {

    private Long id;
    private String title;
    private List<SectionItemResponse> sectionItems;
    
}
