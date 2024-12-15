package com.coigniez.resumebuilder.domain.column;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ColumnRepository extends JpaRepository<Column, Long> {

    @Query("SELECT r.createdBy FROM Column c JOIN c.layout l JOIN l.resume r WHERE c.id = :id")
    Optional<String> findCreatedBy(Long id);

}
