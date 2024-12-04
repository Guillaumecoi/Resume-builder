package com.coigniez.resumebuilder.domain.sectionitem.itemtypes;
import com.coigniez.resumebuilder.domain.sectionitem.SectionItemData;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Textbox implements SectionItemData {

    @NotBlank
    private String content;

}
