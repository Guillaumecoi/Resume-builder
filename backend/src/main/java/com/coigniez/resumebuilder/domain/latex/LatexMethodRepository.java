package com.coigniez.resumebuilder.domain.latex;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LatexMethodRepository extends JpaRepository<LatexMethod, Long> {

} 
