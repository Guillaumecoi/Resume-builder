package com.coigniez.resumebuilder.model.resume.resume;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.coigniez.resumebuilder.model.common.BaseEntity;
import com.coigniez.resumebuilder.model.common.Creatable;
import com.coigniez.resumebuilder.model.common.TimeTrackable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "resume", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"title", "createdBy"})
})
public class Resume implements BaseEntity, TimeTrackable, Creatable {

    @Id
    @GeneratedValue
    private Long id;

    private String title;
    private String firstName;
    private String lastName;
    private String picture;

    @CreatedBy
    @Column(nullable = false, updatable = false)
    private String createdBy;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @Column
    private LocalDateTime lastModifiedDate;

}
