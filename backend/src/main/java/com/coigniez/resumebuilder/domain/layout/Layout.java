package com.coigniez.resumebuilder.domain.layout;

import java.util.List;

import com.coigniez.resumebuilder.domain.columnholder.headerfooter.HeaderFooter;
import com.coigniez.resumebuilder.domain.columnholder.page.LayoutPage;
import com.coigniez.resumebuilder.domain.latex.LatexMethod;
import com.coigniez.resumebuilder.domain.layout.embedded.ColorScheme;
import com.coigniez.resumebuilder.domain.layout.enums.PageSize;
import com.coigniez.resumebuilder.domain.resume.Resume;
import com.coigniez.resumebuilder.interfaces.BaseEntity;

import jakarta.persistence.*;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
@Entity
@Table(name = "layout")
public class Layout implements BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private PageSize pageSize;

    @Embedded
    private ColorScheme colorScheme;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id", referencedColumnName = "id")
    private Resume resume;

    @OneToMany(mappedBy = "layout", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LatexMethod> latexMethods;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LayoutPage> pages;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private HeaderFooter header;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private HeaderFooter footer;

    public void addLatexMethod(LatexMethod latexMethod) {
        latexMethods.add(latexMethod);
        latexMethod.setLayout(this);
    }

    public void removeLatexMethod(LatexMethod latexMethod) {
        latexMethods.remove(latexMethod);
        latexMethod.setLayout(null);
    }

    public void clearLatexMethods() {
        latexMethods.forEach(latexMethod -> latexMethod.setLayout(null));
        latexMethods.clear();
    }

    public void addPage(LayoutPage page) {
        pages.add(page);
        page.setLayout(this);
    }

    public void removePage(LayoutPage page) {
        pages.remove(page);
        page.setLayout(null);
    }

    public void clearPages() {
        pages.forEach(page -> page.setLayout(null));
        pages.clear();
    }
}