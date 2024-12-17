package com.coigniez.resumebuilder.domain.column;

import java.util.ArrayList;
import java.util.List;

import com.coigniez.resumebuilder.domain.columnsection.ColumnSection;
import com.coigniez.resumebuilder.domain.layout.Layout;
import com.coigniez.resumebuilder.domain.layout.enums.ColorLocation;
import com.coigniez.resumebuilder.interfaces.BaseEntity;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "layout_id", referencedColumnName = "id")
    private Layout layout;
    
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

    public void addSectionMapping(ColumnSection sectionMapping) {
        sectionMappings.add(sectionMapping);
        sectionMapping.setColumn(this);
    }

    public void removeSectionMapping(ColumnSection sectionMapping) {
        sectionMappings.remove(sectionMapping);
        sectionMapping.setColumn(null);
    }

    public void clearSectionMappings() {
        sectionMappings.forEach(sectionMapping -> sectionMapping.setColumn(null));
        sectionMappings.clear();
    }
}