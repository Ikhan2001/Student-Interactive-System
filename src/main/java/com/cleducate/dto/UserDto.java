package com.cleducate.dto;

import com.cleducate.entity.Address;
import com.cleducate.entity.Role;
import com.cleducate.enums.CurrentStatus;
import com.cleducate.enums.Gender;
import com.cleducate.enums.Roles;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.Set;


@Data
public class UserDto {

    private Long id;

    @NotBlank(message = "First name cannot be blank")
    @Size(max = 50, message = "First name must be at most 50 characters")
    private String firstName;

    @NotBlank(message = "Last name cannot be blank")
    @Size(max = 50, message = "Last name must be at most 50 characters")
    private String lastName;

    @NotBlank(message = "Phone number cannot be blank")
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid Indian phone number")
    private String phone;


    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email should be valid")
    private String email;

    @URL(message = "Invalid URL format")
    private String pictureUrl;

    @NotBlank(message = "Timezone cannot be blank")
    private String timezone;

    @NotNull(message = "Current status cannot be null")
    private CurrentStatus currentStatus;

    @NotNull(message = "Gender cannot be null")
    private Gender gender;

    @NotNull(message = "Role cannot be null")
    private Roles roles;

    private boolean isActive;
    private boolean isAccountRemoved;

    @Valid// Applies validation recursively on the fields of AddressDto
    private AddressDto address;

}
