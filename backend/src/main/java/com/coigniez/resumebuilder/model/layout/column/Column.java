package com.coigniez.resumebuilder.model.layout.column;

import java.util.ArrayList;
import java.util.List;

import com.coigniez.resumebuilder.model.common.BaseEntity;
import com.coigniez.resumebuilder.model.layout.Layout;
import com.coigniez.resumebuilder.model.layout.enums.ColorLocation;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Entity
@Table(name = "column")
public class Column implements BaseEntity {
    
    @Id
    @GeneratedValue
    private Long id;
    
    @Min(1) @Max(2)
    private Integer columnNumber;
    
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("position ASC")
    private List<ColumnSectionMapping> sectionMappings = new ArrayList<>();
    
    private ColorLocation backgroundColor;
    private ColorLocation textColor;
    
    @Min(0)
    private Double paddingLeft = 10.0;
    @Min(0) 
    private Double paddingRight = 10.0;
    @Min(0)
    private Double paddingTop = 10.0;
    @Min(0)
    private Double paddingBottom = 10.0;
    
    @ManyToOne
    private Layout layout;
}