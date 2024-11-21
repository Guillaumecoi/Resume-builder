package com.coigniez.resumebuilder.model.resume.resume;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ResumeRepository extends JpaRepository<Resume, Long> {

    @Query("""
        SELECT r FROM Resume r WHERE r.createdBy = :user
        """)
    Page<Resume> findAllByCreatedBy(Pageable pageable, String user);

    @Modifying
    @Transactional
    @Query("""
        DELETE FROM Resume r WHERE r.createdBy = :user
        """)
    void deleteAllByCreatedBy(String user);

}
