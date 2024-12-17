package com.coigniez.resumebuilder.domain.resume;

import java.util.ArrayList;
import java.util.List;

import com.coigniez.resumebuilder.domain.section.SectionRequest;
import com.coigniez.resumebuilder.interfaces.ObjectHasID;

import jakarta.validation.constraints.NotBlank;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResumeRequest implements ObjectHasID {

    Long id;

    @NotBlank
    String title;

    @Builder.Default
    List<SectionRequest> sections = new ArrayList<>();

}
