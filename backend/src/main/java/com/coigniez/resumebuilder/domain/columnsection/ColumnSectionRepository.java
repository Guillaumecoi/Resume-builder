package com.coigniez.resumebuilder.domain.columnsection;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ColumnSectionRepository extends JpaRepository<ColumnSection, Long> {

    @Query("SELECT r.createdBy FROM ColumnSection cs JOIN cs.column c JOIN c.layout l JOIN l.resume r WHERE cs.id = :id")
    Optional<String> findCreatedBy(@Param("id") Long id);
    
}