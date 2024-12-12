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
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
@DynamicUpdate
@NamedEntityGraph(name = "resume.sections", attributeNodes = @NamedAttributeNode("sections"))
public class Resume implements BaseEntity, TimeTrackable, Creatable {

    @Id
    @GeneratedValue
    private Long id;

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

    @OneToMany(mappedBy = "resume", fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    @Builder.Default
    private Set<Section> sections = new HashSet<>();

    @OneToMany(mappedBy = "resume", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    @Builder.Default
    private List<Layout> layouts = new ArrayList<>();

    public void addSection(Section section) {
        sections.add(section);
        section.setResume(this);
    }

    public void addLayout(Layout layout) {
        layouts.add(layout);
        layout.setResume(this);
    }
}
