package com.coigniez.resumebuilder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.coigniez.resumebuilder.domain.columnholder.ColumnHolder;

@Repository
public interface ColumnHolderRepository extends JpaRepository<ColumnHolder, Long> {

}
