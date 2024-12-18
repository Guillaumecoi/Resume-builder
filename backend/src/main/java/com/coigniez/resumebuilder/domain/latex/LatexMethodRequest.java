package com.coigniez.resumebuilder.domain.latex;

import com.coigniez.resumebuilder.domain.sectionitem.SectionItemType;
import com.coigniez.resumebuilder.interfaces.ObjectHasID;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LatexMethodRequest implements ObjectHasID {

    private Long id;
    private long layoutId;

    @NotBlank
    private SectionItemType type;
    @NotBlank
    private String name;
    @NotBlank
    private String method;
}
