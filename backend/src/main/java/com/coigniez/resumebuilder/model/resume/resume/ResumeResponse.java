package com.coigniez.resumebuilder.model.resume.resume;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResumeResponse {

    private Long id;
    private String title;
    private String firstName;
    private String lastName;
    private byte[] picture;
    private String createdDate;
    private String lastModifiedDate;

}
