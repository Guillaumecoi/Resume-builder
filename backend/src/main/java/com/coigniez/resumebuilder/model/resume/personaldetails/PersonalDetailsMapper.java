package com.coigniez.resumebuilder.model.resume.personaldetails;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PersonalDetailsMapper {

    PersonalDetailsDto toDto(PersonalDetails personalDetails);

    PersonalDetails toEntity(PersonalDetailsDto personalDetailsDto);

}
