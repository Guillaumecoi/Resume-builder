package com.coigniez.resumebuilder.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.coigniez.resumebuilder.domain.sectionitem.SectionItem;

@Repository
public interface SectionItemRepository extends JpaRepository<SectionItem, Long> {

    @Query("SELECT r.createdBy FROM SectionItem si JOIN si.subSection ss JOIN ss.section s JOIN s.resume r WHERE si.id = :id")
    Optional<String> findCreatedBy(Long id);

}