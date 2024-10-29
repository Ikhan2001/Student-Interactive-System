package com.cleducate.entity.courseAssessment;

import com.cleducate.entity.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Question extends BaseEntity {

    private String question;
    private String hint;
    private int marks;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assessment_id", referencedColumnName = "id")
    private Assessment assessment;

    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Choice> choices;

}
