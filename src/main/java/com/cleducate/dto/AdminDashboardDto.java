package com.cleducate.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdminDashboardDto {
    private int totalUsers;
    private int totalActiveUsers;
    private int totalCourses;
    private int totalActiveCourses;
}
