package com.coigniez.resumebuilder.domain.columnholder.headerfooter;

import com.coigniez.resumebuilder.domain.columnholder.ColumnHolder;
import com.coigniez.resumebuilder.domain.layout.Layout;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@Entity
@DiscriminatorValue("HEADER_FOOTER")
public class Header extends ColumnHolder {

    @Column(precision = 4)
    private Double height;
    private Boolean repeatOnEveryPage;

    @OneToOne
    @JoinColumn(name = "layout_id")
    private Layout layout;

}
