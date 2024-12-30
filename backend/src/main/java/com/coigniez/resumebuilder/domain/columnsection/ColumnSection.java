package com.coigniez.resumebuilder.domain.columnsection;

import java.util.List;

import com.coigniez.resumebuilder.domain.column.Column;
import com.coigniez.resumebuilder.domain.latex.LatexMethod;
import com.coigniez.resumebuilder.domain.layout.enums.AlignmentType;
import com.coigniez.resumebuilder.domain.layoutsection.LayoutSection;
import com.coigniez.resumebuilder.domain.layoutsectionrow.LayoutSectionRow;
import com.coigniez.resumebuilder.interfaces.BaseEntity;
import com.coigniez.resumebuilder.interfaces.LatexMethodProvider;
import com.coigniez.resumebuilder.latex.generators.LatexMethodGenerator;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "column_section")
public class ColumnSection implements BaseEntity, LatexMethodProvider {

    @Id
    @GeneratedValue
    private Long id;

    private Integer itemOrder;
    private double itemsep;
    private double endsep;
    private AlignmentType alignment;
    private boolean hidden;
    private boolean defaultOrder;

    @ManyToOne
    @JoinColumn(name = "column_id", referencedColumnName = "id")
    private Column column;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "latex_method_id", referencedColumnName = "id")
    private LatexMethod latexMethod;

    @OneToOne(fetch = FetchType.EAGER)
    private LayoutSection layoutSection;

    @OneToMany(mappedBy = "columnSection", fetch = FetchType.EAGER)
    private List<LayoutSectionRow> layoutSectionRows;

    public void addLayoutSectionRow(LayoutSectionRow layoutSectionRow) {
        layoutSectionRows.add(layoutSectionRow);
        layoutSectionRow.setColumnSection(this);
    }

    public void removeLayoutSectionRow(LayoutSectionRow layoutSectionRow) {
        layoutSectionRows.remove(layoutSectionRow);
        layoutSectionRow.setColumnSection(null);
    }

    public void clearLayoutSectionRows() {
        for (LayoutSectionRow layoutSectionRow : List.copyOf(layoutSectionRows)) {
            removeLayoutSectionRow(layoutSectionRow);
        }
    }

    @Override
    public List<String> getData() {
        return List.of(
                LatexMethodGenerator.generateUsage(latexMethod.getMethodType(), latexMethod.getType(),
                        latexMethod.getName(),
                        layoutSection.getData()),
                String.valueOf(itemsep) + "pt",
                String.valueOf(endsep) + "pt");
    }
}
