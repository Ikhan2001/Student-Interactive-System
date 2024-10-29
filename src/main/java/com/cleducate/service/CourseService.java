package com.cleducate.service;

import com.cleducate.dto.coursedto.CourseDto;
import com.cleducate.dto.coursedto.CourseResponseDto;
import com.cleducate.dto.coursedto.ModuleDto;
import java.util.List;
import java.util.Map;

public interface CourseService {

    Map<String, Object> createCourse(CourseDto courseDto);
    Map<String, Object> createBulkCourses(List<CourseDto> courseDtoList);
    Map<String, Object> getCourseByCourseId(Long courseId);
    Map<String, Object> getAllCoursesWithDetails(Integer page, Integer count, String sorting);
    Map<String, Object> getAllCourses();
    Map<String, Object> updateCourse(CourseResponseDto responseDto);
    Map<String, Object> removeCourse(Long courseId);

    Map<String, Object> getModuleByModuleId(Long moduleId);
    Map<String, Object> getAllModules(Integer page, Integer count, String sorting);
    Map<String, Object> addModule(ModuleDto moduleDto);
    Map<String, Object> updateModule(ModuleDto moduleDto);
    Map<String, Object> removeModule(Long moduleId);

    Map<String, Object> courseEnrollment(Long UserId, Long courseId);
    Map<String, Object> getEnrollmentDetailsById(Long enrollmentId);
    Map<String, Object> getAllEnrollmentDetails();
    Map<String, Object> updateEnrollUserForCourse(Long enrollmentId,Long userId, Long courseId);
    Map<String, Object> removeEnrollUserForCourse(Long enrollmentId);
}
