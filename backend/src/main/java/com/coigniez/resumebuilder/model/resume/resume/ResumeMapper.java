package com.coigniez.resumebuilder.model.resume.resume;

import org.mapstruct.Mapper;
import com.coigniez.resumebuilder.model.resume.personaldetails.PersonalDetailsMapper;

@Mapper(componentModel = "spring", uses = PersonalDetailsMapper.class)
public interface ResumeMapper {

    ResumeDto toDto(Resume resume);

    Resume toEntity(ResumeDto resumeDto);
}