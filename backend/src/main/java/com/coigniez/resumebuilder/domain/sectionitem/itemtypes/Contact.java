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
public class Contact implements SectionItemData {

    public String icon;
    @NotBlank
    public String label;
    public String link;
    
}
