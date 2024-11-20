package com.coigniez.resumebuilder.model.resume.personaldetails;

import org.springframework.stereotype.Component;

import com.coigniez.resumebuilder.interfaces.Mapper;

@Component
public class PersonalDetailsMapper implements Mapper<PersonalDetails, PersonalDetailsDto, PersonalDetailsDto> {

    public PersonalDetailsDto toDto(PersonalDetails personalDetails) {
        if (personalDetails == null) {
            return null;
        }

        return PersonalDetailsDto.builder()
                .id(personalDetails.getId())
                .firstName(personalDetails.getFirstName())
                .lastName(personalDetails.getLastName())
                .email(personalDetails.getEmail())
                .phone(personalDetails.getPhone())
                .address(personalDetails.getAddress())
                .website(personalDetails.getWebsite())
                .linkedIn(personalDetails.getLinkedIn())
                .github(personalDetails.getGithub())
                .instagram(personalDetails.getInstagram())
                .facebook(personalDetails.getFacebook())
                .build();
    }

    public PersonalDetails toEntity(PersonalDetailsDto personalDetailsDto) {
        if (personalDetailsDto == null) {
            return null;
        }

        return PersonalDetails.builder()
                .id(personalDetailsDto.getId())
                .firstName(personalDetailsDto.getFirstName())
                .lastName(personalDetailsDto.getLastName())
                .email(personalDetailsDto.getEmail())
                .phone(personalDetailsDto.getPhone())
                .address(personalDetailsDto.getAddress())
                .website(personalDetailsDto.getWebsite())
                .linkedIn(personalDetailsDto.getLinkedIn())
                .github(personalDetailsDto.getGithub())
                .instagram(personalDetailsDto.getInstagram())
                .facebook(personalDetailsDto.getFacebook())
                .build();
    }
}