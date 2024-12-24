package com.coigniez.resumebuilder.domain.sectionitem.itemtypes;

import java.util.List;

import com.coigniez.resumebuilder.interfaces.SectionItemData;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Skillboxes implements SectionItemData {
    
    @NotNull
    @NotEmpty
    private List<String> skills;

    public static int getBaseParameterCount() {
        return 1;
    }

    @Override
    @JsonIgnore
    public List<String> getData() {
        List<String> data = List.of(
                skills.toString()
        );

        if (data.size() != getBaseParameterCount()) {
            throw new IllegalStateException("Skillboxes data size does not match base parameter count");
        }

        return data;
    }
}

