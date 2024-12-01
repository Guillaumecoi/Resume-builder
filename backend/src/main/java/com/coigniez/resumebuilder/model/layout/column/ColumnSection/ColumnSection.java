package com.coigniez.resumebuilder.model.layout.column.ColumnSection;

import com.coigniez.resumebuilder.model.common.BaseEntity;
import com.coigniez.resumebuilder.model.layout.column.Column;
import com.coigniez.resumebuilder.model.section.Section;

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

    @ManyToOne
    @NotNull
    private Column column;

    @ManyToOne
    @NotNull
    private Section section;
    @NotNull
    private Integer position;
    private double itemsep;
}
