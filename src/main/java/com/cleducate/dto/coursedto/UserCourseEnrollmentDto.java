package com.cleducate.dto.coursedto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCourseEnrollmentDto {

    private Long id;

    @NotNull
    private ZonedDateTime assignmentDate;

    private ZonedDateTime completionDate;

    @NotNull
    private Integer progressPercentage;

    @NotNull
    private Boolean isCompleted;

    @NotNull
    private Boolean isActive;

    @NotNull
    private Boolean isRemoved;

    @NotNull
    private Long userId;

    @NotNull
    private Long adminId;

    @NotNull
    private Long courseId;
}
