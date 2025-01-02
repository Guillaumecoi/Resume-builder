package com.coigniez.resumebuilder.domain.layoutsectionrow.dtos;

import com.coigniez.resumebuilder.interfaces.UpdateRequest;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
public class LayoutSectionUpdateReq implements UpdateRequest {

    @NotNull
    private Long id;
    private Long latexMethodId;
    @NotNull
    private int rowOrder;

}
