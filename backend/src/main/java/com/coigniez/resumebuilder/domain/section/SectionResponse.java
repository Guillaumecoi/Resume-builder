package com.coigniez.resumebuilder.domain.section;

import java.util.List;

import com.coigniez.resumebuilder.domain.sectionitem.SectionItemResponse;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SectionResponse {

    @NotNull
    private long id;
    @NotBlank
    private String title;
    @NotNull
    private boolean showTitle;
    @NotNull
    private List<SectionItemResponse> sectionItems;
    
}
