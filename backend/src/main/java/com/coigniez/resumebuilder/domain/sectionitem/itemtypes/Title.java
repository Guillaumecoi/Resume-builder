package com.coigniez.resumebuilder.domain.sectionitem.itemtypes;

import java.util.List;
import java.util.Optional;

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
public class Title implements SectionItemData {

    @NotBlank
    private String title;
    private String subtitle;

    public static int getBaseParameterCount() {
        return 2;
    }

    @Override
    @JsonIgnore
    public List<String> getData() {
        List<String> data = List.of(
                title,
                Optional.ofNullable(subtitle).orElse(""));

        if (data.size() != getBaseParameterCount()) {
            throw new IllegalStateException("Title data size does not match base parameter count");
        }

        return data;
    }

}
