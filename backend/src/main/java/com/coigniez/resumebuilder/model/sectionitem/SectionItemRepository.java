package com.coigniez.resumebuilder.model.sectionitem;

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
    @Query("UPDATE SectionItem si SET si.itemOrder = si.itemOrder + 1 WHERE si.section.id = :sectionId AND si.itemOrder >= :startOrder")
    void incrementItemOrderForSection(@Param("sectionId") Long sectionId, @Param("startOrder") int startOrder);

    @Modifying
    @Query("UPDATE SectionItem si SET si.itemOrder = si.itemOrder + 1 WHERE si.section.id = :sectionId AND si.itemOrder BETWEEN :startOrder AND :endOrder")
    void incrementItemOrderForRange(@Param("sectionId") Long sectionId, @Param("startOrder") int startOrder, @Param("endOrder") int endOrder);

    @Modifying
    @Query("UPDATE SectionItem si SET si.itemOrder = si.itemOrder - 1 WHERE si.section.id = :sectionId AND si.itemOrder > :deletedOrder")
    void decrementItemOrderForSection(@Param("sectionId") Long sectionId, @Param("deletedOrder") int deletedOrder);

    @Modifying
    @Query("UPDATE SectionItem si SET si.itemOrder = si.itemOrder - 1 WHERE si.section.id = :sectionId AND si.itemOrder BETWEEN :startOrder AND :endOrder")
    void decrementItemOrderForRange(@Param("sectionId") Long sectionId, @Param("startOrder") int startOrder, @Param("endOrder") int endOrder);

    @Query("SELECT r.createdBy FROM SectionItem si JOIN si.section s JOIN s.resume r WHERE si.id = :id")
    Optional<String> findCreatedBy(@Param("id") Long id);
}