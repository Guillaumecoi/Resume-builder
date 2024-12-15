package com.coigniez.resumebuilder.domain.resume;

import java.util.List;

import com.coigniez.resumebuilder.domain.section.SectionResponse;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResumeDetailResponse {

    private Long id;
    private String title;
    private byte[] picture;
    private String createdDate;
    private String lastModifiedDate;

    private List<SectionResponse> sections;

}