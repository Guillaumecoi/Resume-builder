package com.coigniez.resumebuilder.domain.resume;

import java.time.LocalDateTime;
import java.util.List;

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
@Table(name = "resumes", uniqueConstraints = {
        @UniqueConstraint(name = "uk_resumes_title_created_by", columnNames = {"title", "created_by"})
}, indexes = {
        @Index(name = "idx_resumes_created_by", columnList = "created_by"),
        @Index(name = "idx_resumes_last_modified_date", columnList = "last_modified_date")
})
public class Resume implements BaseEntity, TimeTrackable, Creatable {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String title;

    @CreatedBy
    @Column(nullable = false, updatable = false)
    private String createdBy;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime lastModifiedDate;

    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Section> sections;

    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Layout> layouts;

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
