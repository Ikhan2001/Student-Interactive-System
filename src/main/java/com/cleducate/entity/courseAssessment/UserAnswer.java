package com.cleducate.entity.courseAssessment;

import com.cleducate.entity.BaseEntity;
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
public class UserAnswer extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_assessment_id", referencedColumnName = "id")
    private UserAssessment userAssessment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", referencedColumnName = "id")
    private Question question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "selected_choice_id", referencedColumnName = "id")
    private Choice selectedChoice;

    @ColumnDefault("0")
    private long marks;

    @ColumnDefault("false")
    private boolean hintUsed;

}
