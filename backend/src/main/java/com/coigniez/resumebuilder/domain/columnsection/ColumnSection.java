package com.coigniez.resumebuilder.domain.columnsection;

import com.coigniez.resumebuilder.domain.column.Column;
import com.coigniez.resumebuilder.domain.section.Section;
import com.coigniez.resumebuilder.interfaces.BaseEntity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
@Entity
@Table(name = "column_section")
public class ColumnSection implements BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private Column column;

    @ManyToOne(fetch = FetchType.EAGER)
    @NotNull
    private Section section;

    @NotNull
    private Integer sectionOrder;
    private double itemsep;
    private double endsep;
}
