package com.coigniez.resumebuilder.domain.columnsection;

import com.coigniez.resumebuilder.domain.column.Column;
import com.coigniez.resumebuilder.domain.common.BaseEntity;
import com.coigniez.resumebuilder.domain.section.Section;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
@Entity
@Table(name = "column_section_order", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"column_id", "position"})
})
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
    private Integer position;
    private double itemsep;
    private double endsep;
}
