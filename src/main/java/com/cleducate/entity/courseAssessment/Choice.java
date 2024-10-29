package com.cleducate.entity.courseAssessment;

import com.cleducate.entity.BaseEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Choice extends BaseEntity {

    private String choices;
    private boolean isCorrect;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", referencedColumnName = "id")
    private Question question;

}
