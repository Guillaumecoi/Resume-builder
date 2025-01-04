package com.coigniez.resumebuilder.domain.layout.dtos;

import java.util.List;
import java.util.Set;

import com.coigniez.resumebuilder.domain.columnholder.headerfooter.dtos.HeaderFooterCreateRequest;
import com.coigniez.resumebuilder.domain.columnholder.page.dtos.PageCreateRequest;
import com.coigniez.resumebuilder.domain.latex.dtos.LatexMethodCreateReq;
import com.coigniez.resumebuilder.domain.layout.embedded.ColorScheme;
import com.coigniez.resumebuilder.domain.layout.enums.*;
import com.coigniez.resumebuilder.interfaces.CreateRequest;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LayoutCreateReq implements CreateRequest {

    @NotNull
    private long resumeId;

    private PageSize pageSize;

    private ColorScheme colorScheme;
    private Set<LatexMethodCreateReq> latexMethods;

    private HeaderFooterCreateRequest header;
    private HeaderFooterCreateRequest footer;

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private List<PageCreateRequest> pages;
}