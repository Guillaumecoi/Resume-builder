package com.coigniez.resumebuilder.domain.layout;

import java.util.List;
import java.util.Set;

import com.coigniez.resumebuilder.domain.column.Column;
import com.coigniez.resumebuilder.domain.latex.LatexMethod;
import com.coigniez.resumebuilder.domain.layout.enums.ColorScheme;
import com.coigniez.resumebuilder.domain.layout.enums.PageSize;
import com.coigniez.resumebuilder.domain.resume.Resume;
import com.coigniez.resumebuilder.interfaces.BaseEntity;

import jakarta.persistence.*;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
@Entity
@Table(name = "layout")
public class Layout implements BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    private PageSize pageSize;
    private Integer numberOfColumns;
    private Double columnSeparator;
    @Embedded
    private ColorScheme colorScheme;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id", referencedColumnName = "id")
    private Resume resume;

    @OneToMany(mappedBy = "layout", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("columnNumber ASC")
    private List<Column> columns;

    @OneToMany(mappedBy = "layout", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<LatexMethod> latexMethods;

    public void addColumn(Column column) {
        columns.add(column);
        column.setLayout(this);
    }

    public void removeColumn(Column column) {
        columns.remove(column);
        column.setLayout(null);
    }

    public void clearColumns() {
        columns.forEach(column -> column.setLayout(null));
        columns.clear();
    }

    public void addLatexMethod(LatexMethod latexMethod) {
        latexMethods.add(latexMethod);
        latexMethod.setLayout(this);
    }

    public void removeLatexMethod(LatexMethod latexMethod) {
        latexMethods.remove(latexMethod);
        latexMethod.setLayout(null);
    }

    public void clearLatexMethods() {
        latexMethods.forEach(latexMethod -> latexMethod.setLayout(null));
        latexMethods.clear();
    }
}