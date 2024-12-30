package com.coigniez.resumebuilder.domain.layout;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

import com.coigniez.resumebuilder.domain.column.Column;
import com.coigniez.resumebuilder.domain.latex.LatexMethod;
import com.coigniez.resumebuilder.domain.layout.enums.ColorScheme;
import com.coigniez.resumebuilder.domain.layout.enums.PageSize;
import com.coigniez.resumebuilder.domain.resume.Resume;
import com.coigniez.resumebuilder.interfaces.BaseEntity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id", referencedColumnName = "id")
    private Resume resume;

    @Builder.Default
    @OneToMany(mappedBy = "layout", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("columnNumber ASC")
    private List<Column> columns = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "layout", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<LatexMethod> latexMethods = new HashSet<>();

    @NotNull
    @Enumerated(EnumType.STRING)
    private PageSize pageSize;
    

    @NotNull @Min(1) @Max(2)
    private Integer numberOfColumns;
    
    @NotNull @Min(0) @Max(1)
    private Double columnSeparator;

    @NotNull
    @Embedded
    private ColorScheme colorScheme;

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