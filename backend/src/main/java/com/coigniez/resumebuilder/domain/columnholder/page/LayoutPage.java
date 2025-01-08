package com.coigniez.resumebuilder.domain.columnholder.page;

import com.coigniez.resumebuilder.domain.columnholder.ColumnHolder;
import com.coigniez.resumebuilder.domain.layout.Layout;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@Entity
@DiscriminatorValue("PAGE")
public class LayoutPage extends ColumnHolder {

    @Column(nullable = false, columnDefinition = "smallint")
    private int pageNumber;

    @ManyToOne
    @JoinColumn(name = "layout_id", referencedColumnName = "id")
    private Layout layout;
}
