package com.coigniez.resumebuilder.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.coigniez.resumebuilder.domain.layoutsubsection.LayoutSubSection;

@Repository
public interface LayoutSubSectionRepository extends JpaRepository<LayoutSubSection, Long> {

    @Query("SELECT r.createdBy FROM LayoutSubSection lss JOIN layoutSectionRow lsr JOIN lsr.columnSection cs JOIN cs.column c JOIN c.layout l JOIN l.resume r WHERE lsi.id = :id")
    Optional<String> findCreatedBy(@Param("id") Long id);

}
