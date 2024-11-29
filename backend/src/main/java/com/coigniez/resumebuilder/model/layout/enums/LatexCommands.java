package com.coigniez.resumebuilder.model.layout.enums;

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
            \\cvtitle{#1}
            #3
            \\begin{itemize}[left=0pt, itemsep=#2, label={}, topsep=10pt]
                }{
            \\end{itemize}
            \\vspace{20pt}
        }""";

    private String cvTitle;
    private String educationItem;
    private String experienceItem;
    private String skillItem;
    private String skilltext;
    private String skillbullets;
    private String skillbar;
    private String skillbox;
}
