package com.coigniez.resumebuilder.domain.resume.dtos;

import com.coigniez.resumebuilder.interfaces.Response;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResumeSimpleResp implements Response {

    @NotNull
    private long id;
    @NotNull
    private String title;
    private byte[] picture;
    @NotNull
    private String createdDate;
    @NotNull
    private String lastModifiedDate;

}
