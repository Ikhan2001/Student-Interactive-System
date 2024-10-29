package com.cleducate.entity;

import com.cleducate.entity.courseEnrollment.UserCourseEnrollment;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Course extends BaseEntity{

    private String title;
    private String description;
    private String referenceCode;
    private String thumbnailUrl;
    private boolean isActive;
    @ColumnDefault("false")
    private boolean isRemoved;
    @ColumnDefault("false")
    private boolean isCompleted;


    @OneToMany(mappedBy = "course", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<Module> modules;

    @OneToMany(mappedBy = "course", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    @JsonIgnore
    private Set<UserCourseEnrollment> enrolledUsers;

}
