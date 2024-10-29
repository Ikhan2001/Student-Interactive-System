package com.cleducate.constants;

public class UrlMapping {

    public static final String API_V1 = "/api/v1";
    public static final String PUBLIC_URL = "/api/v1";
    public static final String AUTH = "/auth";
    public static final String ADMIN = "/admin";
    public static final String CLIENT = "/client";


    //Authentication End Point
    public static final String LOGIN = PUBLIC_URL + AUTH + "/login";
    public static final String FORGET_PASSWORD = PUBLIC_URL + AUTH + "/forget-password";
    public static final String RESET_PASSWORD = PUBLIC_URL + AUTH + "/reset-password";
    public static final String ADMIN_LOGIN = PUBLIC_URL + AUTH + "/adminLogin";
    public static final String LOGOUT = PUBLIC_URL + AUTH + "/logout";

    //Admin Preferences
    public static final String REGISTER_CLIENT = API_V1 + ADMIN + "/register-client";
    public static final String CREATE_COURSE = API_V1 + ADMIN + "/create-course";
    public static final String CREATE_BULK_COURSES = API_V1 + ADMIN + "/create-bulk-courses";
    public static final String GET_COURSE_BY_ID = API_V1 + ADMIN + "/course";
    public static final String GET_ALL_COURSES = API_V1 + ADMIN + "/courses";
    public static final String GET_ALL_COURSES_WITH_DETAILS = API_V1 + ADMIN + "/courses-with-details";
    public static final String UPDATE_COURSE = API_V1 + ADMIN + "/update-course";
    public static final String REMOVE_COURSE = API_V1 + ADMIN + "/remove-course";
    public static final String ADMIN_DASHBOARD = API_V1 + ADMIN + "/admin-dashboard";

    //user-course-enrollment
    public static final String USER_COURSE_ENROLLMENT = API_V1 + ADMIN + "/add-user-course-enroll";
    public static final String UPDATE_USER_COURSE_ENROLLMENT = API_V1 + ADMIN + "/update-user-course-enroll";
    public static final String REMOVE_USER_COURSE_ENROLLMENT = API_V1 + ADMIN + "/remove-user-course-enroll";
    public static final String GET_USER_COURSE_ENROLLMENT_BY_ID = API_V1 + ADMIN + "/user-course-enrollment";
    public static final String GET_ALL_USER_COURSE_ENROLLMENT = API_V1 + ADMIN + "/user-course-enrollments";

    //Module
    public static final String ADD_MODULE = API_V1 + ADMIN + "/add-module";
    public static final String UPDATE_MODULE = API_V1 + ADMIN + "/update-module";
    public static final String REMOVE_MODULE = API_V1 + ADMIN + "/remove-module";
    public static final String GET_MODULE_BY_ID = API_V1 + ADMIN + "/module";
    public static final String GET_ALL_MODULES = API_V1 + ADMIN + "/modules";
    //setUserStatus
    public static final String UNBLOCK_USER = API_V1 + ADMIN + "/unblock-user";
    public static final String ACTIVATE_USER_STATUS = API_V1 + ADMIN + "/set-user-status";
    //client Services
    public static final String COMPLETE_PROFILE = PUBLIC_URL + CLIENT + "/complete-profile";
    public static final String GET_USER_BY_ID = API_V1 + ADMIN + "/user";
    public static final String GET_ALL_USERS = API_V1 + ADMIN + "/users";
    public static final String ASSIGN_COURSES = API_V1 + CLIENT + "/assign-courses";
    public static final String GET_COURSE_DETAILS_BY_ID = API_V1 + CLIENT + "/course";



    public static final String APPLICATION_URL = "http://localhost:8081/api/v1/admin";
    public static final String TOKEN = "?token=";

}
