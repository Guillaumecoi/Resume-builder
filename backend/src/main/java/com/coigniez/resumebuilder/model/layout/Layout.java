package com.coigniez.resumebuilder.model.layout;

import java.util.ArrayList;
import java.util.List;

import com.coigniez.resumebuilder.model.common.BaseEntity;
import com.coigniez.resumebuilder.model.layout.enums.ColorScheme;
import com.coigniez.resumebuilder.model.layout.enums.LatexCommands;
import com.coigniez.resumebuilder.model.layout.enums.PageSize;
import com.coigniez.resumebuilder.model.resume.Resume;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "layout")
public class Layout implements BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    // Page Settings
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private PageSize pageSize = PageSize.A4;

    // Culomns
    @Builder.Default
    @OneToMany(mappedBy = "layout", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("columnNumber ASC")
    private List<Column> columns = new ArrayList<>();
    
    @Builder.Default
    @Min(1) @Max(2)
    private Integer numberOfColumns = 1;
    
    @Min(0) @Max(1)
    private Double columnSeparator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id", referencedColumnName = "id")
    private Resume resume;

    @Embedded
    private ColorScheme colorScheme;

    @Embedded
    private LatexCommands latexCommands;
}