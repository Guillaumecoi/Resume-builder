package com.coigniez.resumebuilder.model.layout.enums;

import java.util.Map;

public final class LatexCommands {
    private LatexCommands() {} // Prevent instantiation

    private static final String CV_TITLE = String.join("\n",
        "\\newcommand{\\cvtitle}[1]{",
        "   \\hspace{6pt}\\textbf{\\Large{\\uppercase{#1}}}\\\\[-4pt]",
        "   \\textcolor{accentcolor}{\\rule{80pt}{2pt}}",
        "}"
    );

    private static final String CV_SECTION = String.join("\n",
        "\\newcommand{\\cvsection}[2]{",
        "   \\cvtitle{#1}\\\\[4pt]",
        "   #2\\vspace{20pt}",
        "}"
    );

    private static final String EDUCATION_ITEM = String.join("\n",
        "\\newcommand{\\educationitem}[3]{",
        "   \\textbf{#1} \\newline",
        "   \\textit{#2} \\newline",
        "   #3",
        "}"
    );

    private static final String EXPERIENCE_ITEM = String.join("\n",
        "\\newcommand{\\experienceitem}[4]{",
        "   \\textbf{#1} \\newline",
        "   \\textit{#2} \\newline",
        "   #3 \\newline",
        "   #4",
        "}"
    );
        
    public static final Map<String, String> DEFAULT_COMMANDS = Map.of(
        "cvtitle", CV_TITLE,
        "cvsection", CV_SECTION,
        "educationitem", EDUCATION_ITEM,
        "experienceitem", EXPERIENCE_ITEM
    );
}
