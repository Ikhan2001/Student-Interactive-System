package com.cleducate.repository;

import com.cleducate.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query(value = "SELECT * FROM course c WHERE c.id =:courseId", nativeQuery = true)
    Course findCourseById(Long courseId);

    @Query(value = "SELECT * FROM course", nativeQuery = true)
    List<Course> getAllCourses();

    @Query(value = "SELECT * FROM course", nativeQuery = true)
    Page<Course> getAllCoursesWithDetails(Pageable pageable);

    @Query(value = "Select count(c.id) from course c", nativeQuery = true)
    int totalCourses();

    @Query(value = "Select count(c.id) from course c where c.is_active = true", nativeQuery = true)
    int totalActiveCourses();

}
