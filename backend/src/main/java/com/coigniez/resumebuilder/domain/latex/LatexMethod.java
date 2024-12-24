package com.coigniez.resumebuilder.domain.latex;

import java.util.List;
import java.util.ArrayList;

import com.coigniez.resumebuilder.domain.layout.Layout;
import com.coigniez.resumebuilder.domain.sectionitem.SectionItem;
import com.coigniez.resumebuilder.interfaces.BaseEntity;
import com.coigniez.resumebuilder.interfaces.LatexMethodProvider;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
@Entity
@Table(name = "latex_method")
public class LatexMethod implements BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "layout_id", referencedColumnName = "id")
    private Layout layout;

    @Builder.Default
    @OneToMany(mappedBy = "latexMethod", fetch = FetchType.LAZY, orphanRemoval = false)
    private List<SectionItem> sectionItems = new ArrayList<>();

    @NotNull
    @Enumerated(EnumType.STRING)
    private HasLatexMethod type;

    @NotBlank
    private String name;

    @NotBlank
    @Column(columnDefinition = "TEXT")
    private String method;

    public void addSectionItem(SectionItem sectionItem) {
        if (type.getDataType() != sectionItem.getItem().getClass()) {
            throw new IllegalArgumentException("SectionItem data type does not match the method type");
        }
        sectionItems.add(sectionItem);
        sectionItem.setLatexMethod(this);
    }

    public void removeSectionItem(SectionItem sectionItem) {
        sectionItems.remove(sectionItem);
        sectionItem.setLatexMethod(null);
    }

    /**
     * Generates the LaTeX command using the provided SectionItemData.
     *
     * @param provider the SectionItemData instance containing parameters
     * @return the LaTeX command as a string
     */
    public String generateCommand(LatexMethodProvider provider) {
        List<String> parameters = provider.getData();

        // Start with the command name
        StringBuilder command = new StringBuilder("\\" + name);
        // Append parameters one by one inside braces
        for (String parameter : parameters) {
            command.append("{" + parameter + "}");
        }
        return command.toString();
    }

    /**
     * Generates the LaTeX method using the provided information.
     *
     * @return the LaTeX method as a string
     */
    public String generateMethod() {
        String header = "\\newcommand{\\" + name + "}[" + type.getNumberOfParameters() + "]\n";
        return header + method;
    }

}
