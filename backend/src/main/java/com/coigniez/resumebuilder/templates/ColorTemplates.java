package com.coigniez.resumebuilder.templates;

import com.coigniez.resumebuilder.domain.layout.enums.ColorScheme;

public class ColorTemplates {

    public static final ColorScheme EXECUTIVE_SUITE = ColorScheme.builder()
            .name("Executive Suite")
            .primaryColor("#31323C")
            .secondaryColor("#dad7d5")
            .accent("#613B2E")
            .darkBg("#31323C")
            .lightBg("#E8E8EA")
            .darkText("#31323C")
            .lightText("#dad7d5")
            .build();

    public static final ColorScheme MODERN = ColorScheme.builder()
            .name("Moka Green")
            .primaryColor("#283618")
            .secondaryColor("#606c38")
            .accent("#bc6c25")
            .darkBg("#283618")
            .lightBg("#fefae0")
            .darkText("#283618")
            .lightText("#fefae0")
            .build();

    public static final ColorScheme CLASSIC = ColorScheme.builder()
            .name("Marine red")
            .primaryColor("#252422")
            .secondaryColor("#ccc5b9")
            .accent("#780000")
            .darkBg("#003049")
            .lightBg("#fff4ed")
            .darkText("#003049")
            .lightText("#fff4ed")
            .build();

}
