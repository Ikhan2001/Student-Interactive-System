package com.cleducate.service;

import com.cleducate.dto.ClientProfileRequestDto;

import java.util.Map;

public interface ClientService {
    Map<String, Object> completeProfile( ClientProfileRequestDto profileRequestDto);
    Map<String, Object> getUserDetailsByUserId(Long userId);
    Map<String, Object> getAllUsersDetails();
    Map<String, Object> getAssignCourses();
}
