package com.coigniez.resumebuilder.model.resume.resume;

import com.coigniez.resumebuilder.model.resume.personaldetails.PersonalDetailsDto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResumeResponse {

    private Long id;
    private String title;
    private PersonalDetailsDto personalDetails;
    private String createdDate;
    private String lastModifiedDate;

}
