package com.coigniez.resumebuilder.domain.subsection;

import java.util.ArrayList;
import java.util.List;

import com.coigniez.resumebuilder.domain.section.Section;
import com.coigniez.resumebuilder.domain.sectionitem.SectionItem;
import com.coigniez.resumebuilder.interfaces.BaseEntity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "subsection")
public class SubSection implements BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String title;
    private String icon;
    private boolean showTitle;

    @ManyToOne
    @JoinColumn(name = "section_id", referencedColumnName = "id")
    private Section section;

    @OneToMany(mappedBy = "subsection", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SectionItem> items;

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
}
