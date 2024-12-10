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
        \\newenvironment{cvsection}[4]{
            \\def\\cvsectionvspace{#3}%
            \\ifthenelse{\\equal{#1}{}}{}{\\sectiontitle{#1}}
            #4
            \\begin{itemize}[left=0pt, itemsep=#2, label={}, topsep=10pt]
                }{
            \\end{itemize}
            \\vspace{\\cvsectionvspace}
        }""";

    @Column(columnDefinition = "text")
    private String sectionTitle;
    @Column(columnDefinition = "text")
    private String title;
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
            sectionTitle,
            title,
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
