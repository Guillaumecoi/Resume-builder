package com.coigniez.resumebuilder.domain.subsection.dtos;

import java.util.List;

import com.coigniez.resumebuilder.domain.sectionitem.dtos.SectionItemResp;
import com.coigniez.resumebuilder.interfaces.Response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SubSectionResponse implements Response {

    @NotNull
    private long id;

    @NotBlank
    private String title;
    private String icon;
    @NotNull
    private boolean showTitle;

    @NotNull
    private List<SectionItemResp> sectionItems;
    
}