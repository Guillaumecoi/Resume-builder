package com.coigniez.resumebuilder.domain.section;

import java.util.ArrayList;
import java.util.List;

import com.coigniez.resumebuilder.domain.sectionitem.SectionItemRequest;
import com.coigniez.resumebuilder.interfaces.ObjectHasID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SectionRequest implements ObjectHasID {

    Long id;
    @NotNull
    long resumeId;
    @NotBlank
    String title;
    @Builder.Default
    Boolean showTitle = true;
    @Builder.Default
    List<SectionItemRequest> sectionItems = new ArrayList<>();

}
