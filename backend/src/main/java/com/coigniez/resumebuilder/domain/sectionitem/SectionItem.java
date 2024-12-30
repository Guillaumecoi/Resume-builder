package com.coigniez.resumebuilder.domain.sectionitem;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.coigniez.resumebuilder.domain.subsection.SubSection;
import com.coigniez.resumebuilder.interfaces.BaseEntity;
import com.coigniez.resumebuilder.interfaces.SectionItemData;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "section_item")
public class SectionItem implements BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    private Integer itemOrder;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private SectionItemData item;

    @ManyToOne
    @JoinColumn(name = "subsection_id", referencedColumnName = "id")
    private SubSection section;
}