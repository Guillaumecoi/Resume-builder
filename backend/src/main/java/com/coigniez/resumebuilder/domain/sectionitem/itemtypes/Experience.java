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
public class Experience implements SectionItemData {

    @NotBlank
    private String jobTitle;
    private String companyName;
    private String period;
    private String description;
    private List<String> responsibilities;

    @JsonIgnore
    public String getResponsibilitiesAsItems() {
        if (responsibilities == null) {
            return "";
        }
        return responsibilities.stream()
                .map(r -> "\\item " + r.trim())
                .reduce((a, b) -> a + "\n" + b)
                .orElse("");
    }

    public static int getBaseParameterCount() {
        return 5;
    }

    @Override
    @JsonIgnore
    public List<String> getData() {
        List<String> data = List.of(
                jobTitle,
                Optional.ofNullable(companyName).orElse(""),
                Optional.ofNullable(period).orElse(""),
                Optional.ofNullable(description).orElse(""),
                getResponsibilitiesAsItems());

        if (data.size() != getBaseParameterCount()) {
            throw new IllegalStateException("Experience data size does not match base parameter count");
        }

        return data;
    }
}
