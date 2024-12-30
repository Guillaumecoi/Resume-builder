package com.coigniez.resumebuilder.domain.section;

import java.util.ArrayList;
import java.util.List;

import com.coigniez.resumebuilder.domain.resume.Resume;
import com.coigniez.resumebuilder.domain.subsection.SubSection;
import com.coigniez.resumebuilder.interfaces.BaseEntity;

import jakarta.persistence.*;
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

    private String title;
    private String icon;
    private boolean showTitle;

    @ManyToOne
    @JoinColumn(name = "resume_id", referencedColumnName = "id")
    private Resume resume;

    @OneToMany(mappedBy = "section", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SubSection> subSections;

    public void addSubSection(SubSection subSection) {
        subSections.add(subSection);
        subSection.setSection(this);
    }

    public void removeSubSection(SubSection subSection) {
        subSections.remove(subSection);
        subSection.setSection(null);
    }

    public void clearSubSections() {
        for (SubSection subSection : new ArrayList<>(subSections)) {
            removeSubSection(subSection);
        }
    }

}
