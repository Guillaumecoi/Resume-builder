package com.coigniez.resumebuilder.model.resume.personaldetails;

import com.coigniez.resumebuilder.model.common.BaseEntity;
import com.coigniez.resumebuilder.model.common.ResumeItem;
import com.coigniez.resumebuilder.model.resume.resume.Resume;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Entity
public class PersonalDetails implements BaseEntity, ResumeItem {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne(mappedBy = "personalDetails", fetch = FetchType.LAZY)
    @JsonIgnore
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
