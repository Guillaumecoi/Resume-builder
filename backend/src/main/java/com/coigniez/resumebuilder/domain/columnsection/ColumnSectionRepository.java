package com.coigniez.resumebuilder.domain.columnsection;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ColumnSectionRepository extends JpaRepository<ColumnSection, Long> {

    @Query("SELECT MAX(cs.sectionOrder) FROM ColumnSection cs WHERE cs.column.id = :columnId")
    Optional<Integer> findMaxSectionOrderByColumnId(@Param("columnId") Long columnId);

    @Modifying
    @Query("""
        UPDATE ColumnSection cs 
        SET cs.sectionOrder = cs.sectionOrder + 1 
        WHERE cs.column.id = :columnId 
        AND cs.sectionOrder >= :newOrder 
        AND cs.sectionOrder < :oldOrder
    """)
    void incrementSectionOrderBetween(@Param("columnId") Long columnId, @Param("newOrder") int newOrder, @Param("oldOrder") int oldOrder);

    @Modifying
    @Query("""
        UPDATE ColumnSection cs 
        SET cs.sectionOrder = cs.sectionOrder - 1 
        WHERE cs.column.id = :columnId 
        AND cs.sectionOrder > :oldOrder 
        AND cs.sectionOrder <= :newOrder
    """)
    void decrementSectionOrderBetween(@Param("columnId") Long columnId, @Param("newOrder") int newOrder, @Param("oldOrder") int oldOrder);


    @Query("SELECT cs FROM ColumnSection cs WHERE cs.column.id = :columnId ORDER BY cs.sectionOrder")
    List<ColumnSection> findAllByColumnId(long columnId);

    @Modifying
    @Query("DELETE FROM ColumnSection cs WHERE cs.column.id = :columnId")
    void removeAllByColumnId(long columnId);

    @Query("SELECT cs FROM ColumnSection cs WHERE cs.section.id = :id")
    Optional<ColumnSection> findAllBySectionId(@Param("id") Long id);

    @Modifying
    @Query("DELETE FROM ColumnSection cs WHERE cs.section.id = :sectionId")
    void removeAllBySectionId(long sectionId);


    @Query("SELECT r.createdBy FROM ColumnSection cs JOIN cs.column c JOIN c.layout l JOIN l.resume r WHERE cs.id = :id")
    Optional<String> findCreatedBy(@Param("id") Long id);
    
}