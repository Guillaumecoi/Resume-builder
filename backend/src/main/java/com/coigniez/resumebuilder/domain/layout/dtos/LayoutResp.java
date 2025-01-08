package com.coigniez.resumebuilder.domain.layout.dtos;

import java.util.List;

import com.coigniez.resumebuilder.domain.columnholder.headerfooter.dtos.HeaderResp;
import com.coigniez.resumebuilder.domain.columnholder.page.dtos.PageResp;
import com.coigniez.resumebuilder.domain.latex.dtos.LatexMethodResp;
import com.coigniez.resumebuilder.domain.layout.embedded.ColorScheme;
import com.coigniez.resumebuilder.domain.layout.enums.*;
import com.coigniez.resumebuilder.interfaces.Response;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LayoutResp implements Response {

    @NotNull
    private Long id;
    @NotNull
    private PageSize pageSize;
    @NotNull
    private ColorScheme colorScheme;
    @NotNull
    private List<LatexMethodResp> latexMethods;
    @NotNull
    private List<PageResp> pages;
    private HeaderResp header;
    
}