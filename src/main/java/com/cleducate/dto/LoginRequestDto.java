package com.cleducate.dto;

import com.cleducate.enums.Platform;
import com.cleducate.enums.Roles;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Accessors(chain = true)
@Getter
@Setter
public class LoginRequestDto {

    @NotNull(message = "Login by can't be blank")
    @NotNull(message = "Role can't be blank")
    private Roles loginBy;

    @NotBlank(message = "Email can't be blank")
    private String email;

    @NotBlank(message = "Password can't be blank")
    private String password;

    private Platform platform;

    @NotBlank(message = "Timezone can't be blank")
    private String timeZone;

    public LoginRequestDto(){

    }














}
