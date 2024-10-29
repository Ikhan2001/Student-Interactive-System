package com.cleducate.dto.coursedto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseDto {

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

    private boolean isActive;
    private boolean isRemoved;
    private boolean isDeleted;

    private Set<ModuleDto> modules;


}