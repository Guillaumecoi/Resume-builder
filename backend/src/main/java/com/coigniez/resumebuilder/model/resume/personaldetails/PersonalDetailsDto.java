package com.coigniez.resumebuilder.model.resume.personaldetails;

import com.coigniez.resumebuilder.model.resume.resume.Resume;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PersonalDetailsDto {

    private Long id;
    private Resume resume;
    private String firstName;
    private String lastName;
    private String picture;
    private String email;
    private String phone;
    private String address;
    private String website;
    private String linkedIn;
    private String github;
    private String instagram;
    private String facebook;

}
