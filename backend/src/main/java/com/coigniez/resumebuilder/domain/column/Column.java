package com.coigniez.resumebuilder.domain.column;

import java.util.List;

import com.coigniez.resumebuilder.domain.columnsection.ColumnSection;
import com.coigniez.resumebuilder.domain.layout.Layout;
import com.coigniez.resumebuilder.domain.layout.enums.ColorLocation;
import com.coigniez.resumebuilder.interfaces.BaseEntity;

import jakarta.persistence.*;
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
    
    private int columnNumber;

    @ManyToOne
    @JoinColumn(name = "layout_id", referencedColumnName = "id")
    private Layout layout;
    
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ColumnSection> sectionMappings;
    
    private ColorLocation backgroundColor;
    private ColorLocation textColor;
    private ColorLocation borderColor;
    
    private double paddingLeft;
    private double paddingRight;
    private double paddingTop;
    private double paddingBottom;

    private double borderLeft;
    private double borderRight;
    private double borderTop;
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