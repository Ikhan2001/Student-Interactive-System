package com.cleducate.service;

import com.cleducate.dto.LoginRequestDto;

import java.util.Map;;

public interface AuthenticationService {

    Map<String, Object> authenticateUserAccount(LoginRequestDto credentials);
    Map<String, Object> forgetPassword(String email);
    Map<String, Object> resetPassword(String token, String password, String confirmPassword);
}
