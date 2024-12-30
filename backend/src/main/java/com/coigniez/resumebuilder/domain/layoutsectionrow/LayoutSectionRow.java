package com.coigniez.resumebuilder.domain.layoutsectionrow;

import java.util.List;

import com.coigniez.resumebuilder.domain.columnsection.ColumnSection;
import com.coigniez.resumebuilder.domain.latex.LatexMethod;
import com.coigniez.resumebuilder.domain.layoutsubsection.LayoutSubSection;
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
@Table(name = "layout_sectionrow")
public class LayoutSectionRow implements BaseEntity, LatexMethodProvider {

    @Id
    @GeneratedValue
    private Long id;

    private int rowOrder;

    @OneToOne
    private ColumnSection columnSection;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "latex_method_id", referencedColumnName = "id")
    private LatexMethod latexMethod;

    @OneToMany(mappedBy = "layoutSectionRow", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LayoutSubSection> layoutSubSections;

    @Override
    public List<String> getData() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getData'");
    }
}