package com.coigniez.resumebuilder.domain.section;

import java.util.ArrayList;
import java.util.List;

import com.coigniez.resumebuilder.domain.columnsection.ColumnSection;
import com.coigniez.resumebuilder.domain.resume.Resume;
import com.coigniez.resumebuilder.domain.sectionitem.SectionItem;
import com.coigniez.resumebuilder.interfaces.BaseEntity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "section")
public class Section implements BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    private String title;
    private String icon;

    @NotNull
    private boolean showTitle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id", referencedColumnName = "id")
    private Resume resume;

    @OneToMany(mappedBy = "section", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<SectionItem> items = new ArrayList<>();

    @OneToMany(mappedBy = "section", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ColumnSection> columnSections = new ArrayList<>();

    public void addSectionItem(SectionItem item) {
        items.add(item);
        item.setSection(this);
    }

    public void removeSectionItem(SectionItem item) {
        items.remove(item);
        item.setSection(null);
    }

    public void clearSectionItems() {
        for (SectionItem item : new ArrayList<>(items)) {
            removeSectionItem(item);
        }
    }

    public void addColumnSection(ColumnSection columnSection) {
        columnSections.add(columnSection);
        columnSection.setSection(this);
    }

    public void removeColumnSection(ColumnSection columnSection) {
        columnSections.remove(columnSection);
        columnSection.setSection(null);
    }

    public void clearColumnSections() {
        columnSections.forEach(columnSection -> columnSection.setSection(null));
        columnSections.clear();
    }

}
