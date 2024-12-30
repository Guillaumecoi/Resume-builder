package com.coigniez.resumebuilder.domain.sectionitem.itemtypes;
import java.util.List;

import com.coigniez.resumebuilder.interfaces.SectionItemData;
import com.fasterxml.jackson.annotation.JsonIgnore;

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


    @Override
    @JsonIgnore
    public List<String> getData() {
        return List.of(content);
    }

}
