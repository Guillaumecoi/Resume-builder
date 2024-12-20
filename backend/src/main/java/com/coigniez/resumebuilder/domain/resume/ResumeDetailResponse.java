package com.coigniez.resumebuilder.domain.resume;

import java.util.List;

import com.coigniez.resumebuilder.domain.section.SectionResponse;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResumeDetailResponse {

    @NotNull
    private long id;
    @NotBlank
    private String title;
    private byte[] picture;
    @NotNull
    private String createdDate;
    @NotNull
    private String lastModifiedDate;

    private List<SectionResponse> sections;

}