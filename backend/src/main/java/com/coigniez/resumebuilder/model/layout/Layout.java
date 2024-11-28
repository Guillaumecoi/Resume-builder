package com.coigniez.resumebuilder.model.layout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.coigniez.resumebuilder.model.common.BaseEntity;
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
@Entity
@Table(name = "layout")
public class Layout implements BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    // Page Settings
    @Enumerated(EnumType.STRING)
    private PageSize pageSize = PageSize.A4;

    // Colors
    @Column(length = 7)
    private String primaryColor = "#000000";
    @Column(length = 7)
    private String secondaryColor = "#444444";
    @Column(length = 7)
    private String accentColor = "#0066cc";
    @Column(length = 7)
    private String backgroundColor = "#ffffff";

    // Culomns
    @OneToMany(mappedBy = "layout", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("columnNumber ASC")
    private List<Column> columns = new ArrayList<>();
    
    @Min(1) @Max(2)
    private Integer numberOfColumns = 1;
    
    @Min(0) @Max(1)
    private Double columnSeparator = 0.35;

    // Latex Commands
    @ElementCollection
    private Map<String, String> latexCommands = new HashMap<>(LatexCommands.DEFAULT_COMMANDS);

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id", referencedColumnName = "id")
    private Resume resume;
}