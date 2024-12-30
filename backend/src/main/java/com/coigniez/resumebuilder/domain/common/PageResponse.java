package com.coigniez.resumebuilder.domain.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import jakarta.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageResponse<T> {
    @NotNull
    private List<T> content;
    @NotNull
    private int number;
    @NotNull
    private int size;
    @NotNull
    private long totalElements;
    @NotNull
    private int totalPages;
    @NotNull
    private boolean first;
    @NotNull
    private boolean last;
}
