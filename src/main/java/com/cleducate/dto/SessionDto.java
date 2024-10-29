package com.cleducate.dto;

import com.cleducate.enums.Platform;
import lombok.Data;
import java.util.Date;

@Data
public class SessionDto {

    private UserDto user;

    private String authToken;

    private Date loginTime;
    private Date lastLoginTime;

    private Platform platform;

    private boolean isExpired;
}
