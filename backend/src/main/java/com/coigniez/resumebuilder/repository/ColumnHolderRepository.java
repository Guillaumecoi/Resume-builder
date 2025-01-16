package com.coigniez.resumebuilder.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.coigniez.resumebuilder.domain.columnholder.ColumnHolder;

@Repository
public interface ColumnHolderRepository extends JpaRepository<ColumnHolder, Long> {

    @Query("""
            SELECT COALESCE(r1.createdBy, r2.createdBy)
            FROM ColumnHolder ch
            LEFT JOIN Header h ON h.id = ch.id
            LEFT JOIN LayoutPage p ON p.id = ch.id
            LEFT JOIN h.layout l1
            LEFT JOIN p.layout l2
            LEFT JOIN l1.resume r1
            LEFT JOIN l2.resume r2
            WHERE ch.id = :id
            """)
    Optional<String> findCreatedBy(@Param("id") long id);
}