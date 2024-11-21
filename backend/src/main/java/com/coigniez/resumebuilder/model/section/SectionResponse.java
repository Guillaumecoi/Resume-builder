package com.coigniez.resumebuilder.model.section;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SectionResponse {

    private Long id;
    private String title;
    
}
