package com.coigniez.resumebuilder.domain.latex;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LatexMethodRepository extends JpaRepository<LatexMethod, Long> {

    @Query("SELECT lm FROM LatexMethod lm JOIN lm.layout l WHERE l.id = :layoutId")
    List<LatexMethod> findAllByLayoutId(long layoutId);

    @Modifying
    @Query("DELETE FROM LatexMethod lm WHERE lm.layout.id = :layoutId")
    void deleteAllByLayoutId(long layoutId);

    @Query("SELECT r.createdBy FROM LatexMethod lm JOIN lm.layout l JOIN l.resume r WHERE lm.id = :id")
    Optional<String> findCreatedBy(long id);
}
