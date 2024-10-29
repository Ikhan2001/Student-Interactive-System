package com.cleducate.dto.coursedto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class ModuleDto {

    private Long id;

    @NotBlank(message = "Title cannot be blank")
    @Size(min = 3, max = 128, message = "Title must be between 3 and 128 characters")
    private String title;

    @NotBlank(message = "Description cannot be blank")
    private String description;

    @NotBlank(message = "Code cannot be blank")
    @Size(min = 3, max = 16, message = "Code must be between 3 and 16 characters")
    private String code;

    private String thumbnailUrl;

    private String lectureUrl;

    private boolean isCompleted;
    private boolean isRemoved;
    private boolean isActive;
    private boolean isDeleted;

    private Long courseId;

}