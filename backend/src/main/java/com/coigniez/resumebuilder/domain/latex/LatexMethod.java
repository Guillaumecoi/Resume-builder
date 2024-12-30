package com.coigniez.resumebuilder.domain.latex;

import java.util.List;

import com.coigniez.resumebuilder.domain.columnsection.ColumnSection;
import com.coigniez.resumebuilder.domain.layout.Layout;
import com.coigniez.resumebuilder.interfaces.BaseEntity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
@Entity
@Table(name = "latex_method")
public class LatexMethod implements BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "layout_id", referencedColumnName = "id")
    private Layout layout;

    @OneToMany(mappedBy = "latexMethod", fetch = FetchType.LAZY)
    private List<ColumnSection> columnSections;

    @NotNull
    @Enumerated(EnumType.STRING)
    private HasLatexMethod type;

    @NotBlank
    private String name;

    @NotNull
    private MethodType methodType;

    @NotBlank
    @Column(columnDefinition = "TEXT")
    private String method;


    public void addColumnSection(ColumnSection columnSection) {
        columnSections.add(columnSection);
        columnSection.setLatexMethod(this);
    }

    public void removeColumnSection(ColumnSection columnSection) {
        columnSections.remove(columnSection);
        columnSection.setLatexMethod(null);
    }

}
