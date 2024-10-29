package com.cleducate.entity.courseAssessment;

import com.cleducate.entity.BaseEntity;
import com.cleducate.entity.Course;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Assessment extends BaseEntity {

    private String title;
    private String description;
    private long totalMarks;
    private long duration;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", referencedColumnName = "id")
    private Course course;

    @OneToMany(mappedBy = "assessment", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Question> questions;





}
