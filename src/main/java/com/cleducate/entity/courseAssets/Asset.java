package com.cleducate.entity.courseAssets;

import com.cleducate.entity.BaseEntity;
import com.cleducate.entity.Course;
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
public class Asset extends BaseEntity {

    private String title;
    private String description;

    private String fileUrl;
    private String fileType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", referencedColumnName = "id")
    private  Course course;

    @ColumnDefault("true")
    private boolean isActive;

}
