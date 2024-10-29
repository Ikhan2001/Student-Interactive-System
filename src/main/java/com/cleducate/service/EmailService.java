package com.cleducate.service;

import com.cleducate.entity.Course;
import com.cleducate.entity.User;
import com.cleducate.entity.courseEnrollment.UserCourseEnrollment;

public interface EmailService {


    void sendEmailToClientOnRegistration(User user, String password);
    void sendEmailOnProfileCompletion(User user);
    void sendEmailToAdminOnCourseEnrollment(UserCourseEnrollment userCourseEnrollment);
    void sendEmailToClientOnCourseEnrollment(UserCourseEnrollment userCourseEnrollment);
    void sendEmailToUserOnForgetPassword(User user, String resetLink);
    void sendEmailOnAccountBlock(User user);
    void sendEmailOnAccountUnBlock(User user, String resetLink);
}
