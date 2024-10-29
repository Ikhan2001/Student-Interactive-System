package com.cleducate.constants;

import com.cleducate.enums.CourseSorting;
import com.cleducate.enums.ModuleSoring;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.stream.Collectors;

public class MessageCode {

    public static final String USER_ACCOUNT_NOT_FOUND = "User account not found !!!";
    public static final String INVALID_USER_ID = "User not found with userId, please use valid userId !!!";
    public static final String INVALID_USER_ROLE = "User must be CLIENT !!!";
    public static final String USER_DELETED = "Your account is deleted. We will remove your data within 60 days. Contact support team for any help !!!";
    public static final String USER_DISABLED = "Your account is blocked. Contact support team for any help";
    public static final String VALID_CREDENTIAL = "Login successfully.";
    public static final String INVALID_CREDENTIAL = "Invalid username or password. Please try again.";
    public static final String INVALID_TIMEZONE = "Invalid timezone, Please enter a valid timezone !!!";

    public static final String CLIENT_REGISTERED_SUCCESSFULLY = "Client Registered Successfully !!!";
    public static final String CLIENT_PROFILE_CREATED = "Client Profile is Successfully Created.";
    public static final String CLIENT_PROFILE_INCOMPLETE = "Your profile is incomplete. Please provide the required details to complete your profile.";
    public static final String CLIENT_EMAIL_DOES_NOT_EXIST = "Client email is invalid , Please use a valid email";

    //admin
    public static final String ACCESS_DENIED = "Access denied. Admin privileges required.";
    public static final String USER_UNBLOCKED_SUCCESSFULLY = "User has been successfully unblocked.";
    public static final String USER_ACTIVATED_SUCCESSFULLY = "User has been successfully activated.";
    public static final String ADMIN_DASHBOARD_DATA_FETCHED_SUCCESSFULLY = "Admin dashboard data fetched successfully!";


    //course
    public static final String COURSE_CREATED = "Course has been Successfully created.";
    public static final String COURSES_CREATED = "Courses has been Successfully created.";
    public static final String COURSE_DATA_INVALID = "Invalid course data provided. Please check the details and try again.";
    public static final String INVALID_COURSE_ID = "Invalid course ID. Please check the ID and try again.";
    public static final String COURSE_NOT_FOUND = "Course not found. Please check the courses and try again.";
    public static final String INVALID_COURSE_SORTING = "Invalid sorting type. Please provide a valid sorting+ option: "+ Arrays.stream(CourseSorting.values()).map(Enum::name).collect(Collectors.toList());
    public static final String COURSE_UPDATED = "Courses has been Successfully Updated.";
    public static final String COURSE_REMOVED = "Courses has been Successfully Removed.";
    public static final String USER_COURSE_NOT_FOUND = "No course has been assigned to the user. Please check the user’s enrollment status.";
    public static final String COURSE_FETCHED_SUCCESSFULLY = "Course details fetched successfully.";

    //course enrollment
    public static final String INVALID_ENROLLMENT_ID = "Invalid Enrollment ID. Please check the ID and try again.";
    public static final String ENROLLMENT_DETAILS_NOT_FOUND = "Enrollment Details not found. Please check the Enrollment details and try again.";
    public static final String ENROLLMENT_DETAILS_UPDATED = "Enrollment Details has been Successfully Updated.";
    public static final String ENROLLMENT_REMOVED = "Enrollment User Course Details has been Successfully Removed.";


    public static final String INVALID_MODULE_ID = "Invalid Module ID. Please check the ID and try again.";
    public static final String MODULE_ADDED = "Module has been Successfully Added";
    public static final String MODULE_UPDATED = "Module has been Successfully Updated";
    public static final String MODULE_REMOVED = "Module has been Successfully Removed";
    public static final String MODULE_NOT_FOUND = "Module not found. Please check the Modules and try again.";
    public static final String INVALID_MODULE_SORTING = "Invalid sorting type. Please provide a valid sorting+ option: "+ Arrays.stream(ModuleSoring.values()).map(Enum::name).collect(Collectors.toList());

    //users
    public static final String USER_DETAILS_FETCH_SUCCESSFULLY = "User Details Fetch Successfully !!!";
    public static final String USER_SUCCESSFULLY_ENROLLED = "User has been Successfully Enrolled !!!";
    public static final String USER_ALREADY_ENROLLED_IN_COURSE = "The user is already enrolled in this course. Please assign a different user or course.";
    public static final String USER_NOT_ENROLLED_IN_COURSE = "The user is not enrolled in this course. Please assign the user to the course first.";
    public static final String PASSWORD_RESET_LINK_SENT = "A password reset link has been sent to your registered email address. Please check your inbox to reset your password.";
    public static final String INVALID_TOKEN_MESSAGE = "The token provided is invalid, expired, or has already been used. Please request a new one.";
    public static final String PASSWORD_RESET_SUCCESS_MESSAGE = "Your password has been successfully reset. You can now log in with your new credentials.";
    public static final String ACCOUNT_BLOCKED_MESSAGE = "Your account has been blocked after 3 failed login attempts. Please contact the administrator for assistance.";
    public static final String PASSWORD_MISMATCH_MESSAGE = "The password and confirmation password do not match. Please try again.";
























}
