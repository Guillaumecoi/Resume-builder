package com.coigniez.resumebuilder.domain.latex.dtos;

import com.coigniez.resumebuilder.domain.sectionitem.enums.SectionItemType;
import com.coigniez.resumebuilder.interfaces.UpdateRequest;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateLatexMethodRequest implements UpdateRequest {

    @NotNull
    private Long id;

    @NotNull
    private SectionItemType type;
    @NotBlank
    private String name;
    @NotBlank
    private String method;
}
