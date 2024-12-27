package com.coigniez.resumebuilder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.coigniez.resumebuilder.domain.layoutsectionItem.LayoutSectionItem;

@Repository
public interface LayoutSectionItemRepository extends JpaRepository<LayoutSectionItem, Long> {

}
