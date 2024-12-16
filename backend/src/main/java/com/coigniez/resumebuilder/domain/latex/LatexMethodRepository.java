package com.coigniez.resumebuilder.domain.latex;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LatexMethodRepository extends JpaRepository<LatexMethod, Long> {

    @Query("SELECT r.createdBy FROM LatexMethod lm JOIN lm.layout l JOIN l.resume r WHERE lm.id = :id")
    Optional<String> findCreatedBy(long id);

} 
