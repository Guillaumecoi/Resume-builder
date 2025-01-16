package com.coigniez.resumebuilder.domain.column;

import java.util.List;

import com.coigniez.resumebuilder.domain.column.enums.BackgroungImage;
import com.coigniez.resumebuilder.domain.columnholder.ColumnHolder;
import com.coigniez.resumebuilder.domain.columnsection.ColumnSection;
import com.coigniez.resumebuilder.domain.layout.enums.ColorLocation;
import com.coigniez.resumebuilder.interfaces.BaseEntity;
import com.coigniez.resumebuilder.interfaces.LatexMethodProvider;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "layout_column")
public class LayoutColumn implements BaseEntity, LatexMethodProvider {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, columnDefinition = "SMALLINT")
    private short columnNumber;

    @Column(nullable = false, columnDefinition = "DECIMAL(2,1)")
    private Float ColumnSize;

    private ColorLocation backgroundColor;
    private ColorLocation textColor;
    private ColorLocation borderColor;

    @Column(nullable = false, columnDefinition = "DECIMAL(4,1)")
    private Float paddingLeft;
    @Column(nullable = false, columnDefinition = "DECIMAL(4,1)")
    private Float paddingRight;
    @Column(nullable = false, columnDefinition = "DECIMAL(4,1)")
    private Float paddingTop;
    @Column(nullable = false, columnDefinition = "DECIMAL(4,1)")
    private Float paddingBottom;

    @Column(nullable = false, columnDefinition = "DECIMAL(4,1)")
    private Float borderLeft;
    @Column(nullable = false, columnDefinition = "DECIMAL(4,1)")
    private Float borderRight;
    @Column(nullable = false, columnDefinition = "DECIMAL(4,1)")
    private Float borderTop;
    @Column(nullable = false, columnDefinition = "DECIMAL(4,1)")
    private Float borderBottom;

    @Embedded
    private BackgroungImage backgroundImage;

    @ManyToOne
    @JoinColumn(name = "column_holder_id", referencedColumnName = "id")
    private ColumnHolder columnHolder;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ColumnSection> sectionMappings;

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

    @Override
    public List<String> getData() {
        return List.of(
                String.valueOf(backgroundColor),
                String.valueOf(textColor),
                String.valueOf(borderColor),
                String.valueOf(paddingLeft),
                String.valueOf(paddingRight),
                String.valueOf(paddingTop),
                String.valueOf(paddingBottom),
                String.valueOf(borderLeft),
                String.valueOf(borderRight),
                String.valueOf(borderTop),
                String.valueOf(borderBottom)
        );
    }
}