package com.coigniez.resumebuilder.domain.sectionitem.dtos;

import com.coigniez.resumebuilder.domain.layout.enums.AlignmentType;
import com.coigniez.resumebuilder.interfaces.SectionItemData;
import com.coigniez.resumebuilder.interfaces.UpdateRequest;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateSectionItemRequest implements UpdateRequest {

    @NotNull
    private Long id;

    @NotNull
    private long sectionId;
    @NotNull
    private long latexMethodId;

    @NotNull
    private Integer itemOrder;
    private AlignmentType alignment;

    @NotNull
    private SectionItemData item;

}