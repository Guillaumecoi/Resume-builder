package com.coigniez.resumebuilder.model.section;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SectionRepository extends JpaRepository<Section, Long> {
    
    @Query("""
            SELECT r.createdBy FROM Section s JOIN s.resume r WHERE s.id = :id
            """)
    Optional<String> findCreatedBy(Long id);

}