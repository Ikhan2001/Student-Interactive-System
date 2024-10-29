package com.cleducate.entity.courseAssessment;

import com.cleducate.entity.BaseEntity;
import com.cleducate.entity.User;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.Set;

@Entity
@Table(name = "user_assessment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserAssessment extends BaseEntity {

    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne(fetch =  FetchType.LAZY)
    @JoinColumn(name = "admin_id", referencedColumnName = "id")
    private User admin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assessment_id", referencedColumnName = "id")
    private Assessment assessment;

    @ColumnDefault("false")
    private boolean isCompleted;

    private long TotalMarksObtain;
    private long TotalMarksPossible;

    private ZonedDateTime startTime;
    private ZonedDateTime endTime;

    @OneToMany(mappedBy = "userAssessment", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<UserAnswer> userAnswers;



}
