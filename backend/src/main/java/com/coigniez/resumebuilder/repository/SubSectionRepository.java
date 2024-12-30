package com.coigniez.resumebuilder.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.coigniez.resumebuilder.domain.subsection.SubSection;

public interface SubSectionRepository extends JpaRepository<SubSection, Long> {

    @Query("SELECT r.createdBy FROM SubSection ss JOIN ss.section s JOIN s.resume r WHERE si.id = :id")
    Optional<String> findCreatedBy(Long id);
}
