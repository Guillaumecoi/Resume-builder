package com.coigniez.resumebuilder.domain.layoutsubsection;

import java.util.ArrayList;
import java.util.List;

import com.coigniez.resumebuilder.domain.latex.LatexMethod;
import com.coigniez.resumebuilder.domain.layout.enums.AlignmentType;
import com.coigniez.resumebuilder.domain.layoutsectionItem.LayoutSectionItem;
import com.coigniez.resumebuilder.domain.layoutsectionrow.LayoutSectionRow;
import com.coigniez.resumebuilder.domain.subsection.SubSection;
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
@Table(name = "layout_subsection")
public class LayoutSubSection implements BaseEntity, LatexMethodProvider {

    @Id
    @GeneratedValue
    private Long id;

    private Integer sectionOrder;
    private AlignmentType alignment;
    private Boolean hidden;
    private Boolean defaultOrder;

    @ManyToOne
    @JoinColumn(name = "layout_sectionrow_id", referencedColumnName = "id")
    private LayoutSectionRow layoutSectionRow;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "subsection_id", referencedColumnName = "id")
    private SubSection subsection;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "latex_method_id", referencedColumnName = "id")
    private LatexMethod latexMethod;

    @OneToMany(mappedBy = "layout_subsection", fetch = FetchType.EAGER)
    private List<LayoutSectionItem> layoutSectionItems;

    public void addLayoutSectionItem(LayoutSectionItem layoutSectionItem) {
        layoutSectionItems.add(layoutSectionItem);
        layoutSectionItem.setLayoutSubSection(this);
    }

    public void removeLayoutSectionItem(LayoutSectionItem layoutSectionItem) {
        layoutSectionItems.remove(layoutSectionItem);
        layoutSectionItem.setLayoutSubSection(null);
    }

    public void clearLayoutSectionItems() {
        for (LayoutSectionItem layoutSectionItem : new ArrayList<>(layoutSectionItems)) {
            removeLayoutSectionItem(layoutSectionItem);
        }
    }

    @Override
    public List<String> getData() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getData'");
    }
}
