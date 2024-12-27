package com.coigniez.resumebuilder.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.coigniez.resumebuilder.domain.sectionitem.SectionItem;

@Repository
public interface SectionItemRepository extends JpaRepository<SectionItem, Long> {

    @Query("SELECT MAX(si.itemOrder) FROM SectionItem si WHERE si.section.id = :sectionId")
    Optional<Integer> findMaxItemOrderBySectionId(@Param("sectionId") Long sectionId);

    @Query("SELECT si FROM SectionItem si WHERE si.section.id = :sectionId")
    List<SectionItem> findAllBySectionId(@Param("sectionId") Long sectionId);

    @Modifying
    @Query("DELETE FROM SectionItem si WHERE si.section.id = :sectionId")
    void deleteAllBySectionId(Long sectionId);

    @Query("SELECT r.createdBy FROM SectionItem si JOIN si.section s JOIN s.resume r WHERE si.id = :id")
    Optional<String> findCreatedBy(Long id);

}