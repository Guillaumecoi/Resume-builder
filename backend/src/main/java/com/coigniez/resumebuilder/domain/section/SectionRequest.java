package com.coigniez.resumebuilder.domain.section;

import java.util.ArrayList;
import java.util.List;

import com.coigniez.resumebuilder.domain.sectionitem.SectionItemRequest;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SectionRequest {

    Long id;
    long resumeId;
    @NotNull
    @NotEmpty
    String title;
    @NotNull
    @Builder.Default
    Boolean showTitle = true;
    @Builder.Default
    List<SectionItemRequest> sectionItems = new ArrayList<>();

}
