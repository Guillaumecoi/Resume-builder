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
public class Education implements SectionItemData {

    @NotBlank
    private String degree;
    @NotBlank
    private String institution;
    private String period;
    private String description;

    public static int getBaseParameterCount() {
        return 4;
    }

    @Override
    @JsonIgnore
    public List<String> getData() {
        List<String> data = List.of(
                degree,
                institution,
                Optional.ofNullable(period).orElse(""),
                Optional.ofNullable(description).orElse(""));

        if (data.size() != getBaseParameterCount()) {
            throw new IllegalStateException("Education data size does not match base parameter count");
        }

        return data;
    }
}
