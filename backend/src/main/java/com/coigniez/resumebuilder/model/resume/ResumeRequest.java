package com.coigniez.resumebuilder.model.resume;

import java.util.ArrayList;
import java.util.List;

import com.coigniez.resumebuilder.model.section.SectionRequest;

import jakarta.validation.constraints.NotBlank;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResumeRequest {

    Long id;

    @NotBlank
    String title;

    @Builder.Default
    List<SectionRequest> sections = new ArrayList<>();

}
