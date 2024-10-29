package com.cleducate.dto.coursedto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class CourseResponseDto {
    private Long id;

    @NotBlank(message = "Title cannot be blank")
    @Size(min = 3, max = 128, message = "Title must be between 3 and 128 characters")
    private String title;

    @NotBlank(message = "Description cannot be blank")
    private String description;

    @NotBlank(message = "Reference code cannot be blank")
    @Size(min = 3, max = 16, message = "Reference code must be between 3 and 16 characters")
    private String referenceCode;

    private String thumbnailUrl;

    private boolean isCompleted;
    private boolean isRemoved;
    private boolean isActive;
}
