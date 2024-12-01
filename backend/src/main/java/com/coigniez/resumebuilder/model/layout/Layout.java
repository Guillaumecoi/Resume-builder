package com.coigniez.resumebuilder.model.layout;

import java.util.List;

import com.coigniez.resumebuilder.model.common.BaseEntity;
import com.coigniez.resumebuilder.model.layout.enums.ColorScheme;
import com.coigniez.resumebuilder.model.layout.enums.LatexCommands;
import com.coigniez.resumebuilder.model.layout.enums.PageSize;
import com.coigniez.resumebuilder.model.resume.Resume;
import com.coigniez.resumebuilder.model.layout.column.Column;

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

    @Embedded
    @NotNull
    private LatexCommands latexCommands;

    public void addColumn(Column column) {
        columns.add(column);
        column.setLayout(this);
    }
}