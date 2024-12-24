package com.coigniez.resumebuilder.domain.columnsection;

import java.util.List;
import java.util.Optional;

import com.coigniez.resumebuilder.domain.column.Column;
import com.coigniez.resumebuilder.domain.section.Section;
import com.coigniez.resumebuilder.interfaces.BaseEntity;
import com.coigniez.resumebuilder.interfaces.LatexMethodProvider;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
@Entity
@Table(name = "column_section")
public class ColumnSection implements BaseEntity, LatexMethodProvider {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Column column;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    private Section section;

    @NotNull
    private Integer sectionOrder;
    @NotNull
    private double itemsep;
    @NotNull
    private double endsep;

    public static int getBaseParameterCount() {
        return 3;
    }

    @Override
    public List<String> getData() {
        List<String> data = List.of(
            section.isShowTitle() ? section.getTitle() : "",
            Optional.ofNullable(section.getIcon()).orElse(""),
            String.valueOf(itemsep),
            String.valueOf(endsep)
        );

        if (data.size() != getBaseParameterCount()) {
            throw new IllegalStateException("ColumnSection data size does not match base parameter count");
        }

        return data;
    }
}
