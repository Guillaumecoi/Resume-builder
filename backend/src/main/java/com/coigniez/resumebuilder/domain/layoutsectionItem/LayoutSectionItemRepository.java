package com.coigniez.resumebuilder.domain.layoutsectionItem;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LayoutSectionItemRepository extends JpaRepository<LayoutSectionItem, Long> {

    @Query("SELECT MAX(lsi.itemOrder) FROM LayoutSectionItem lsi WHERE lsi.layoutSection.id = :layoutSectionId")
    Optional<Integer> findMaxItemOrderByLayoutSectionId(@Param("layoutSectionId") Long layoutSectionId);

    @Modifying
    @Query("""
                UPDATE LayoutSectionItem lsi
                SET lsi.itemOrder = lsi.itemOrder + 1
                WHERE lsi.layoutSection.id = :layoutSectionId
                AND lsi.itemOrder >= :newOrder
                AND lsi.itemOrder < :oldOrder
            """)
    void incrementItemOrderBetween(@Param("layoutSectionId") Long layoutSectionId, @Param("newOrder") int newOrder,
            @Param("oldOrder") int oldOrder);

    @Modifying
    @Query("""
                UPDATE LayoutSectionItem lsi
                SET lsi.itemOrder = lsi.itemOrder - 1
                WHERE lsi.layoutSection.id = :layoutSectionId
                AND lsi.itemOrder > :oldOrder
                AND lsi.itemOrder <= :newOrder
            """)
    void decrementItemOrderBetween(@Param("layoutSectionId") Long layoutSectionId, @Param("newOrder") int newOrder,
            @Param("oldOrder") int oldOrder);

    @Query("SELECT lsi FROM LayoutSectionItem lsi WHERE lsi.layoutSection.id = :layoutSectionId")
    List<LayoutSectionItem> findAllByLayoutSectionId(@Param("layoutSectionId") Long layoutSectionId);

    @Modifying
    @Query("DELETE FROM LayoutSectionItem lsi WHERE lsi.layoutSection.id = :layoutSectionId")
    void deleteAllByLayoutSectionId(Long layoutSectionId);

    @Query("SELECT r.createdBy FROM LayoutSectionItem lsi JOIN lsi.layoutSection ls JOIN ls.resume r WHERE lsi.id = :id")
    Optional<String> findCreatedBy(Long id);

}
