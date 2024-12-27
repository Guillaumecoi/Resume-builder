package com.coigniez.resumebuilder.domain.columnsection;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.coigniez.resumebuilder.domain.column.Column;
import com.coigniez.resumebuilder.domain.latex.LatexMethod;
import com.coigniez.resumebuilder.domain.layout.enums.AlignmentType;
import com.coigniez.resumebuilder.domain.layoutsectionItem.LayoutSectionItem;
import com.coigniez.resumebuilder.domain.section.Section;
import com.coigniez.resumebuilder.interfaces.BaseEntity;
import com.coigniez.resumebuilder.interfaces.LatexMethodProvider;
import com.coigniez.resumebuilder.latex.generators.LatexMethodGenerator;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "column_id", referencedColumnName = "id")
    private Column column;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "section_id", referencedColumnName = "id")
    private Section section;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "latex_method_id", referencedColumnName = "id")
    private LatexMethod latexMethod;

    @NotNull
    private Integer itemOrder;
    @NotNull
    private double itemsep;
    @NotNull
    private double endsep;
    private AlignmentType alignment;
    @NotNull
    private boolean hidden;
    @NotNull
    private boolean defaultOrder;

    @OneToMany(mappedBy = "columnSection", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Builder.Default
    private List<LayoutSectionItem> layoutSectionItems = new ArrayList<>();

    public void addLayoutSectionItem(LayoutSectionItem layoutSectionItem) {
        layoutSectionItems.add(layoutSectionItem);
        layoutSectionItem.setColumnSection(this);
    }

    public void removeLayoutSectionItem(LayoutSectionItem layoutSectionItem) {
        layoutSectionItems.remove(layoutSectionItem);
        layoutSectionItem.setColumnSection(null);
    }

    public void clearLayoutSectionItems() {
        layoutSectionItems.forEach(layoutSectionItem -> layoutSectionItem.setColumnSection(null));
        layoutSectionItems.clear();
    }

    @Override
    public List<String> getData() {
        return List.of(
                LatexMethodGenerator.generateUsage(latexMethod.getMethodType(), latexMethod.getType(),
                        latexMethod.getName(), latexMethod.getMethod(),
                        List.of(section.isShowTitle() ? section.getTitle() : "",
                                Optional.ofNullable(section.getIcon()).orElse(""))),
                String.valueOf(itemsep) + "pt",
                String.valueOf(endsep) + "pt");
    }
}
