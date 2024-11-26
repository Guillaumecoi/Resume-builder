package com.coigniez.resumebuilder.model.sectionitem;

import java.util.Map;

import com.coigniez.resumebuilder.model.common.BaseEntity;
import com.coigniez.resumebuilder.model.section.Section;
import com.coigniez.resumebuilder.model.sectionitem.itemtypes.SectionItemType;
import com.coigniez.resumebuilder.model.sectionitem.itemtypes.Skill;
import com.coigniez.resumebuilder.model.sectionitem.itemtypes.WorkExperience;
import com.coigniez.resumebuilder.model.sectionitem.itemtypes.Education;
import com.coigniez.resumebuilder.model.sectionitem.itemtypes.Textbox;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.Valid;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "section_item")
public class SectionItem implements BaseEntity {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id", referencedColumnName = "id")
    private Section section;

    @Enumerated(EnumType.STRING)
    private SectionItemType type;

    private Integer itemOrder;

    @Valid
    @JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
    )
    @JsonSubTypes({
        @JsonSubTypes.Type(value = WorkExperience.class, name = "WORK_EXPERIENCE"),
        @JsonSubTypes.Type(value = Skill.class, name = "SKILL"),
        @JsonSubTypes.Type(value = Education.class, name = "EDUCATION"),
        @JsonSubTypes.Type(value = Textbox.class, name = "TEXTBOX")
    })
    @Column(columnDefinition = "jsonb")
    @Convert(converter = MapConverter.class)
    private Map<String, Object> data;
}