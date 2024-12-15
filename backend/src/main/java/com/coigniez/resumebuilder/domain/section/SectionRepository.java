package com.coigniez.resumebuilder.domain.section;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SectionRepository extends JpaRepository<Section, Long> {

    @Query("SELECT s FROM Section s LEFT JOIN FETCH s.items i WHERE s.id = :id ORDER BY i.itemOrder ASC")
    Optional<Section> findByIdWithOrderedItems(@Param("id") Long id);

    
    @Query("SELECT r.createdBy FROM Section s JOIN s.resume r WHERE s.id = :id")
    Optional<String> findCreatedBy(@Param("id") Long id);

    @Query("SELECT s FROM Section s JOIN s.resume r WHERE r.id = :id")
    List<Section> findAllByResumeId(@Param("id") Long id);

}