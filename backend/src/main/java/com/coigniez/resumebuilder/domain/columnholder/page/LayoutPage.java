package com.coigniez.resumebuilder.domain.columnholder.page;

import com.coigniez.resumebuilder.domain.columnholder.ColumnHolder;
import com.coigniez.resumebuilder.domain.layout.Layout;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@Entity
@DiscriminatorValue("PAGE")
public class LayoutPage extends ColumnHolder {

    @NotNull
    private int pageNumber;

    @ManyToOne
    @JoinColumn(name = "layout_id", referencedColumnName = "id")
    private Layout layout;
}
