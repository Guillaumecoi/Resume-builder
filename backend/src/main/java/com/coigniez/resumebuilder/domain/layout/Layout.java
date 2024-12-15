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
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
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

    // Page Settings
    @Enumerated(EnumType.STRING)
    private PageSize pageSize;

    // Culomns
    @OneToMany(mappedBy = "layout", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("columnNumber ASC")
    private List<Column> columns;
    
    @NotNull @Min(1) @Max(2)
    private Integer numberOfColumns;
    
    @NotNull @Min(0) @Max(1)
    private Double columnSeparator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id", referencedColumnName = "id")
    private Resume resume;

    @Embedded
    @NotNull
    private ColorScheme colorScheme;

    @OneToMany(mappedBy = "layout", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<LatexMethod> latexMethods;

    @NotBlank
    private String sectionTitleMethod;

    public void addColumn(Column column) {
        columns.add(column);
        column.setLayout(this);
    }

    public void removeColumn(Column column) {
        columns.remove(column);
        column.setLayout(null);
    }
}