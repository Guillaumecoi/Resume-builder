package com.coigniez.resumebuilder.domain.layout.dtos;

import java.util.List;
import java.util.Set;

import com.coigniez.resumebuilder.domain.columnholder.header.dtos.HeaderSimpleCreateReq;
import com.coigniez.resumebuilder.domain.columnholder.page.dtos.PageSimpleCreateReq;
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
    private Long resumeId;

    private PageSize pageSize;

    private ColorScheme colorScheme;
    private Set<LatexMethodCreateReq> latexMethods;

    private HeaderSimpleCreateReq header;

    @JsonSetter(nulls = Nulls.AS_EMPTY)
    private List<PageSimpleCreateReq> pages;
}