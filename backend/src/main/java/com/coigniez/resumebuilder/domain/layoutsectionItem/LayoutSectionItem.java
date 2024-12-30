package com.coigniez.resumebuilder.domain.layoutsectionItem;

import com.coigniez.resumebuilder.domain.columnsection.ColumnSection;
import com.coigniez.resumebuilder.domain.latex.LatexMethod;
import com.coigniez.resumebuilder.domain.layout.enums.AlignmentType;
import com.coigniez.resumebuilder.domain.sectionitem.SectionItem;
import com.coigniez.resumebuilder.interfaces.BaseEntity;

import jakarta.persistence.*;
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "section_item_id", referencedColumnName = "id")
    private SectionItem sectionItem;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "latex_method_id", referencedColumnName = "id")
    private LatexMethod latexMethod;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "column_section_id", referencedColumnName = "id")
    private ColumnSection columnSection;

    private boolean hidden;
    private Integer itemOrder;
    private AlignmentType alignment;
    
}
