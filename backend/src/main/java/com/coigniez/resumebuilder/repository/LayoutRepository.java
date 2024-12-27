package com.coigniez.resumebuilder.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.coigniez.resumebuilder.domain.layout.Layout;

@Repository
public interface LayoutRepository extends JpaRepository<Layout, Long> {

    @Query("SELECT l FROM Layout l JOIN l.resume r WHERE r.id = :id")
    List<Layout> findAllByResumeId(@Param("id") long id);

    @Query("SELECT r.createdBy FROM Layout l JOIN l.resume r WHERE l.id = :id")
    Optional<String> findCreatedBy(@Param("id") long id);
    
}