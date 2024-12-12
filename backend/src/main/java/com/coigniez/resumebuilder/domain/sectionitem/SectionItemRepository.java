package com.coigniez.resumebuilder.domain.sectionitem;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SectionItemRepository extends JpaRepository<SectionItem, Long> {

    @Query("SELECT MAX(si.itemOrder) FROM SectionItem si WHERE si.section.id = :sectionId")
    Integer findMaxItemOrderBySectionId(@Param("sectionId") Long sectionId);

    @Modifying
    @Query("UPDATE SectionItem si SET si.itemOrder = si.itemOrder + 1 WHERE si.section.id = :sectionId AND si.itemOrder >= :startOrder")
    void incrementItemOrderForSection(@Param("sectionId") Long sectionId, @Param("startOrder") int startOrder);

    @Modifying
    @Query("UPDATE SectionItem si SET si.itemOrder = si.itemOrder - 1 WHERE si.section.id = :sectionId AND si.itemOrder > :startOrder")
    void decrementItemOrderForSection(Long sectionId, int startOrder);

    @Query("SELECT si FROM SectionItem si WHERE si.section.id = :sectionId")
    List<SectionItem> findAllBySectionId(@Param("sectionId") Long sectionId);
    
    @Modifying
    @Query("DELETE FROM SectionItem si WHERE si.section.id = :sectionId")
    void deleteAllBySectionId(Long sectionId);
    
}