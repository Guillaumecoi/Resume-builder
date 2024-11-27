package com.coigniez.resumebuilder.model.resume;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ResumeRepository extends JpaRepository<Resume, Long> {

    @EntityGraph(value = "Resume.withSections")
    Optional<Resume> findById(long id);

    @Query("SELECT r.createdBy FROM Resume r WHERE r.id = :id")
    Optional<String> findCreatedBy(@Param("id") Long id);

    @Query("SELECT r FROM Resume r WHERE r.createdBy = :user")
    Page<Resume> findAllByCreatedBy(Pageable pageable, String user);

    @Query("SELECT r FROM Resume r WHERE r.createdBy = :user")
    List<Resume> findAllByCreatedBy(String user);

    @Modifying
    @Transactional
    @Query("DELETE FROM Resume r WHERE r.createdBy = :user")
    void deleteAllByCreatedBy(String user);

}
