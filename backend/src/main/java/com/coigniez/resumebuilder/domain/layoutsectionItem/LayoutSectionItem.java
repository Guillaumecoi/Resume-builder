package com.coigniez.resumebuilder.domain.layoutsectionItem;

import com.coigniez.resumebuilder.domain.columnsection.ColumnSection;
import com.coigniez.resumebuilder.domain.latex.LatexMethod;
import com.coigniez.resumebuilder.domain.layout.enums.AlignmentType;
import com.coigniez.resumebuilder.domain.sectionitem.SectionItem;
import com.coigniez.resumebuilder.interfaces.BaseEntity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "layout_sectionitem")
public class LayoutSectionItem implements BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    private SectionItem sectionItem;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    private LatexMethod latexMethod;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private ColumnSection columnSection;

    @NotNull
    private boolean hidden;
    private Integer itemOrder;
    private AlignmentType alignment;
    
}
