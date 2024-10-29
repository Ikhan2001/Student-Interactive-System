package com.cleducate.entity;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class Module extends BaseEntity{

    private String title;
    private String description;
    private String code;
    private String thumbnailUrl;
    private String lectureUrl;
    @ColumnDefault("false")
    private boolean isCompleted;
    @ColumnDefault("false")
    private boolean isRemoved;
    @ColumnDefault("false")
    private boolean isActive;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", referencedColumnName = "id")
    private Course course;
}
