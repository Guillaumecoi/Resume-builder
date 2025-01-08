package com.coigniez.resumebuilder.domain.layout.dtos;

import com.coigniez.resumebuilder.domain.layout.embedded.ColorScheme;
import com.coigniez.resumebuilder.domain.layout.enums.*;
import com.coigniez.resumebuilder.interfaces.UpdateRequest;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LayoutUpdateReq implements UpdateRequest {

    @NotNull
    private Long id;
    private PageSize pageSize;
    private ColorScheme colorScheme;

}