package com.coigniez.resumebuilder.domain.sectionitem;

import java.util.List;
import java.util.Optional;

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
    @Query("""
        UPDATE SectionItem si 
        SET si.itemOrder = si.itemOrder + 1 
        WHERE si.section.id = :sectionId 
        AND si.itemOrder >= :newOrder 
        AND si.itemOrder < :oldOrder
    """)
    void incrementItemOrderBetween(@Param("sectionId") Long sectionId, @Param("newOrder") int newOrder, @Param("oldOrder") int oldOrder);

    @Modifying
    @Query("""
        UPDATE SectionItem si 
        SET si.itemOrder = si.itemOrder - 1 
        WHERE si.section.id = :sectionId 
        AND si.itemOrder > :oldOrder 
        AND si.itemOrder <= :newOrder
    """)
    void decrementItemOrderBetween(@Param("sectionId") Long sectionId, @Param("newOrder") int newOrder, @Param("oldOrder") int oldOrder);

    @Query("SELECT si FROM SectionItem si WHERE si.section.id = :sectionId")
    List<SectionItem> findAllBySectionId(@Param("sectionId") Long sectionId);
    
    @Modifying
    @Query("DELETE FROM SectionItem si WHERE si.section.id = :sectionId")
    void deleteAllBySectionId(Long sectionId);

    @Query("SELECT r.createdBy FROM SectionItem si JOIN si.section s JOIN s.resume r WHERE si.id = :id")
    Optional<String> findCreatedBy(Long id);
    
}