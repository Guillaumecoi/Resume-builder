package com.coigniez.resumebuilder.domain.layout.enums;

import jakarta.persistence.*;
import lombok.*;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@Builder
public class LatexCommands {

    @Builder.Default
    private String cvSection = """
        \\newenvironment{cvsection}[3]{
            \\ifthenelse{\\equal{#1}{}}{}{\\cvtitle{#1}}
            #3
            \\begin{itemize}[left=0pt, itemsep=#2, label={}, topsep=10pt]
                }{
            \\end{itemize}
            \\vspace{20pt}
        }""";

    @Column(columnDefinition = "text")
    private String cvTitle;
    @Column(columnDefinition = "text")
    private String educationItem;
    @Column(columnDefinition = "text")
    private String experienceItem;
    @Column(columnDefinition = "text")
    private String textbox;
    @Column(columnDefinition = "text")
    private String skillitem;
    @Column(columnDefinition = "text")
    private String skilltext;
    @Column(columnDefinition = "text")
    private String skillbullets;
    @Column(columnDefinition = "text")
    private String skillbar;
    @Column(columnDefinition = "text")
    private String skillboxes;
    @Column(columnDefinition = "text")
    private String skillbox;
    @Column(columnDefinition = "text")
    private String picture;

    public String getAllMethods() {
        return String.join("\n",
            cvSection,
            cvTitle,
            educationItem,
            experienceItem,
            textbox,
            skillitem,
            skilltext,
            skillbullets,
            skillbar,
            skillbox,
            skillboxes,
            picture
        );
    }
}
