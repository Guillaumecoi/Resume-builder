package com.coigniez.resumebuilder.domain.sectionitem.dtos;

import com.coigniez.resumebuilder.interfaces.Response;
import com.coigniez.resumebuilder.interfaces.SectionItemData;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SectionItemResp implements Response {

    @NotNull
    private long id;
    
    @NotNull
    private SectionItemData item;

    @NotNull
    private Integer itemOrder;
}