package com.coigniez.resumebuilder.domain.sectionitem.itemtypes;

import com.coigniez.resumebuilder.interfaces.SectionItemData;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Title implements SectionItemData {
    
    private String title;
    private String subtitle;
    
}
