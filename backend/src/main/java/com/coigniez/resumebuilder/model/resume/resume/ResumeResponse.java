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
    private String email;
    private String phone;
    private String address;
    private String website;
    private String linkedIn;
    private String github;
    private String instagram;
    private String facebook;
    private String createdDate;
    private String lastModifiedDate;

}
