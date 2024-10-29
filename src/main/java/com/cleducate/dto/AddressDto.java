package com.cleducate.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class AddressDto {

    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "State Province is required")
    private String stateProvince;

    @NotBlank(message = "ZIP code is required")
    @Pattern(regexp = "^[1-9][0-9]{5}$", message = "Invalid ZIP code format. It should be a six-digit number.")
    private String zipCode;

    @NotBlank(message = "Country is required")
    private String country;
}
