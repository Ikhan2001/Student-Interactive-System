package com.cleducate.repository;

import com.cleducate.entity.courseEnrollment.UserCourseEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserCourseEnrollmentRepository extends JpaRepository<UserCourseEnrollment, Long> {

    @Query(value = "SELECT * FROM user_course_enrollment uce WHERE uce.id =:enrollmentId", nativeQuery = true)
    UserCourseEnrollment findEnrollmentDetailsById(Long enrollmentId);

    @Query(value = "SELECT * FROM user_course_enrollment", nativeQuery = true)
    List<UserCourseEnrollment> getAllUserCourseEnrollmentDetails();

    @Query(value = "SELECT * FROM user_course_enrollment uce WHERE uce.user_id =:userId", nativeQuery = true)
    List<UserCourseEnrollment> getAllUserCourseEnrollmentDetailsByUserId(Long userId);

    @Query(value = "SELECT COUNT(*) > 0 FROM user_course_enrollment uce WHERE uce.user_id =:userId AND uce.course_id =:courseId", nativeQuery = true)
    boolean isUserIsAlreadyEnrolledInCourse(Long userId, Long courseId);

    @Query(value = "SELECT COUNT(*) > 0 FROM user_course_enrollment uce WHERE uce.user_id =:userId", nativeQuery = true)
    boolean isUserExistInEnrolledCourses(Long userId);


}
