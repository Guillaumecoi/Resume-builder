package com.coigniez.resumebuilder.model.layout.column.ColumnSection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ColumnSectionRepository extends JpaRepository<ColumnSection, Long> {
    
}