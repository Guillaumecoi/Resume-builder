package com.coigniez.resumebuilder.domain.sectionitem;

import com.coigniez.resumebuilder.domain.layout.enums.AlignmentType;
import com.coigniez.resumebuilder.interfaces.ObjectHasID;
import com.coigniez.resumebuilder.interfaces.SectionItemData;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SectionItemRequest implements ObjectHasID {

    private Long id;
    @NotNull
    private long sectionId;
    @NotNull
    private long latexMethodId;
    private Integer itemOrder;
    private AlignmentType alignment;

    @NotNull
    private SectionItemData item;

}