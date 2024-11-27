package com.coigniez.resumebuilder.model.sectionitem.itemtypes;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Textbox {
    @NotBlank
    private String content;
}
