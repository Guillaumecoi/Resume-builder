package com.coigniez.resumebuilder.domain.resume.dtos;

import java.util.List;

import com.coigniez.resumebuilder.domain.section.dtos.SectionResp;
import com.coigniez.resumebuilder.interfaces.Response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResumeDetailResponse implements Response {

    @NotNull
    private long id;
    @NotBlank
    private String title;
    private byte[] picture;
    @NotNull
    private String createdDate;
    @NotNull
    private String lastModifiedDate;

    private List<SectionResp> sections;

}