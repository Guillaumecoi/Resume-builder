package com.coigniez.resumebuilder.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.coigniez.resumebuilder.domain.column.LayoutColumn;

@Repository
public interface ColumnRepository extends JpaRepository<LayoutColumn, Long> {
    @Query("""
            SELECT COALESCE(r1.createdBy, r2.createdBy)
            FROM LayoutColumn c
            LEFT JOIN c.columnHolder ch
            LEFT JOIN Header h ON h = ch
            LEFT JOIN LayoutPage p ON p = ch
            LEFT JOIN h.layout l1
            LEFT JOIN p.layout l2
            LEFT JOIN l1.resume r1
            LEFT JOIN l2.resume r2
            WHERE c.id = :id
            """)
    Optional<String> findCreatedBy(long id);
}
