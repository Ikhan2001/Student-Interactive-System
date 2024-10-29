package com.cleducate.entity.courseEnrollment;

import com.cleducate.entity.BaseEntity;
import com.cleducate.entity.Course;
import com.cleducate.entity.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.ZonedDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class UserCourseEnrollment extends BaseEntity {

    @ManyToOne(fetch =  FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne(fetch =  FetchType.EAGER)
    @JoinColumn(name = "admin_id", referencedColumnName = "id")
    private User admin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", referencedColumnName = "id")
    @JsonBackReference
    private Course course;

    private ZonedDateTime assignmentDate;
    private ZonedDateTime completionDate;

    @ColumnDefault("0")
    private int progressPercentage;

    @ColumnDefault("false")
    private boolean isCompleted;
    @ColumnDefault("false")
    private boolean isActive;
    @ColumnDefault("false")
    private boolean isRemoved;


}
