package com.coigniez.resumebuilder.domain.resume;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.coigniez.resumebuilder.domain.layout.Layout;
import com.coigniez.resumebuilder.domain.section.Section;
import com.coigniez.resumebuilder.interfaces.BaseEntity;
import com.coigniez.resumebuilder.interfaces.Creatable;
import com.coigniez.resumebuilder.interfaces.TimeTrackable;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
@NamedEntityGraph(name = "Resume.withSections", attributeNodes = @NamedAttributeNode("sections"))
public class Resume implements BaseEntity, TimeTrackable, Creatable {

    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    private String title;
    private String picture;

    @CreatedBy
    @Column(nullable = false, updatable = false)
    private String createdBy;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column
    private LocalDateTime lastModifiedDate;

    @OneToMany(mappedBy = "resume", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    @Builder.Default
    private Set<Section> sections = new HashSet<>();

    @OneToMany(mappedBy = "resume", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    @Builder.Default
    private List<Layout> layouts = new ArrayList<>();

    public void addSection(Section section) {
        sections.add(section);
        section.setResume(this);
    }

    public void removeSection(Section section) {
        sections.remove(section);
        section.setResume(null);
    }

    public void clearSections() {
        sections.forEach(section -> section.setResume(null));
        sections.clear();
    }

    public void addLayout(Layout layout) {
        layouts.add(layout);
        layout.setResume(this);
    }

    public void removeLayout(Layout layout) {
        layouts.remove(layout);
        layout.setResume(null);
    }

    public void clearLayouts() {
        layouts.forEach(layout -> layout.setResume(null));
        layouts.clear();
    }
}
