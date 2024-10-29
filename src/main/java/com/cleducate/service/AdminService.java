package com.cleducate.service;

import com.cleducate.dto.LoginRequestDto;
import com.cleducate.dto.coursedto.CourseDto;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.List;
import java.util.Map;

public interface AdminService {

    Map<String, Object> adminDashboard();
    Map<String, Object> clientRegistration(LoginRequestDto requestDto);
    Map<String, Object> unblockUser(Long userId);
    Map<String, Object> setUserActivationStatus(Long userId, boolean status);

}
