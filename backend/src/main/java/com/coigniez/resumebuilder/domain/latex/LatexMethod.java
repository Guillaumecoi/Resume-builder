package com.coigniez.resumebuilder.domain.latex;

import com.coigniez.resumebuilder.domain.layout.Layout;
import com.coigniez.resumebuilder.interfaces.BaseEntity;

import jakarta.persistence.*;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
@Entity
@Table(name = "latex_method")
public class LatexMethod implements BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "layout_id", referencedColumnName = "id")
    private Layout layout;

    @Enumerated(EnumType.STRING)
    private HasLatexMethod type;
    private String name;
    private MethodType methodType;
    @Column(columnDefinition = "TEXT")
    private String method;

}
