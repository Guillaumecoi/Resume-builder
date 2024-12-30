package com.coigniez.resumebuilder.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.coigniez.resumebuilder.domain.column.Column;

@Repository
public interface ColumnRepository extends JpaRepository<Column, Long> {

    @Query("SELECT c FROM Column c JOIN c.layout l WHERE l.id = :layoutId")
    List<Column> findAllByLayoutId(long layoutId);

    @Modifying
    @Query("DELETE FROM Column c WHERE c.layout.id = :layoutId")
    void deleteAllByLayoutId(long layoutId);

    @Query("SELECT r.createdBy FROM Column c JOIN c.layout l JOIN l.resume r WHERE c.id = :id")
    Optional<String> findCreatedBy(long id);

}
