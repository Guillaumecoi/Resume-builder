package com.coigniez.resumebuilder.model.layout.column;

import java.util.Map;

import com.coigniez.resumebuilder.model.common.BaseEntity;
import com.coigniez.resumebuilder.model.section.Section;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "column_section_order")
@Getter @Setter
@NoArgsConstructor
public class ColumnSectionMapping implements BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Column column;

    @ManyToOne
    private Section section;

    private Integer position;

    private Map<String, String> latexCommands;
    
    private double itemsep = 8.0;
}
