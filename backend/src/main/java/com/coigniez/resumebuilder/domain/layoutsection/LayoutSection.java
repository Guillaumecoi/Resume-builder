package com.coigniez.resumebuilder.domain.layoutsection;


import java.util.List;

import com.coigniez.resumebuilder.domain.columnsection.ColumnSection;
import com.coigniez.resumebuilder.domain.latex.LatexMethod;
import com.coigniez.resumebuilder.domain.section.Section;
import com.coigniez.resumebuilder.interfaces.BaseEntity;
import com.coigniez.resumebuilder.interfaces.LatexMethodProvider;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "layout_section")
public class LayoutSection implements BaseEntity, LatexMethodProvider {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    private ColumnSection columnSection;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "section_id", referencedColumnName = "id")
    private Section section;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "latex_method_id", referencedColumnName = "id")
    private LatexMethod latexMethod;

    @Override
    public List<String> getData() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getData'");
    }
}