package com.coigniez.resumebuilder.domain.column;

import java.util.ArrayList;
import java.util.List;

import com.coigniez.resumebuilder.domain.columnsection.ColumnSection;
import com.coigniez.resumebuilder.domain.common.BaseEntity;
import com.coigniez.resumebuilder.domain.layout.Layout;
import com.coigniez.resumebuilder.domain.layout.enums.ColorLocation;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
@Entity
@Table(name = "layout_column")
public class Column implements BaseEntity {
    
    @Id
    @GeneratedValue
    private Long id;
    
    @Min(1) @Max(2)
    private Integer columnNumber;
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("position ASC")
    @Builder.Default
    private List<ColumnSection> sectionMappings = new ArrayList<>();
    
    private ColorLocation backgroundColor;
    private ColorLocation textColor;
    private ColorLocation borderColor;
    
    @Min(0)
    private double paddingLeft;
    @Min(0) 
    private double paddingRight;
    @Min(0)
    private double paddingTop;
    @Min(0)
    private double paddingBottom;

    @Min(0)
    private double borderLeft;
    @Min(0)
    private double borderRight;
    @Min(0)
    private double borderTop;
    @Min(0)
    private double borderBottom;
    
    @ManyToOne
    private Layout layout;

    public void addSectionMapping(ColumnSection sectionMapping) {
        sectionMappings.add(sectionMapping);
        sectionMapping.setColumn(this);
    }
}