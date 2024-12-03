package com.coigniez.resumebuilder.model.section;

import java.util.ArrayList;
import java.util.List;

import com.coigniez.resumebuilder.model.common.BaseEntity;
import com.coigniez.resumebuilder.model.layout.column.ColumnSection.ColumnSection;
import com.coigniez.resumebuilder.model.resume.Resume;
import com.coigniez.resumebuilder.model.section.sectionitem.SectionItem;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.persistence.FetchType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    public void addColumnSection(ColumnSection columnSection) {
        columnSections.add(columnSection);
        columnSection.setSection(this);
    }
}
