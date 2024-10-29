package com.cleducate.controller;

import com.cleducate.constants.Literals;
import com.cleducate.constants.UrlMapping;
import com.cleducate.dto.LoginRequestDto;
import com.cleducate.service.AdminService;
import com.cleducate.service.ClientService;
import com.cleducate.service.CourseService;
import com.cleducate.utils.ResponseHandler;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@Slf4j
public class AdminController {

    private final AdminService adminService;
    private final CourseService courseService;
    private final ClientService clientService;

    @Autowired
    public AdminController(AdminService adminService, CourseService courseService, ClientService clientService){
        this.adminService = adminService;
        this.courseService = courseService;
        this.clientService = clientService;
    }

    @Operation(summary = "Admin Dashboard Data", description = "Fetches key metrics and insights for the admin dashboard, including total users, active users, total courses, and active courses.")
    @GetMapping(value = UrlMapping.ADMIN_DASHBOARD)
    public ResponseEntity<Object> getAdminDashbord(){
        try{
            Map<String, Object> resultMap = adminService.adminDashboard() ;
            if(resultMap.get(Literals.STATUS).equals(Literals.TRUE)){
                return ResponseHandler.response(resultMap.get(Literals.RESPONSE), resultMap.get(Literals.MESSAGE).toString(), true, HttpStatus.OK);
            }
            return ResponseHandler.response(null, resultMap.get(Literals.MESSAGE).toString(), false, HttpStatus.BAD_REQUEST);

        }catch (Exception e){
            log.error(Literals.CATCH_EXCEPTION, e.getMessage());
            return ResponseHandler.response(null, Literals.SOMETHING_WENT_WRONG, false, HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Register a client", description = "Registers a new client with the necessary details.")
    @PostMapping(value = UrlMapping.REGISTER_CLIENT)
    public ResponseEntity<Object> registerClient(@Valid @RequestBody LoginRequestDto credentials){
        try{
            Map<String, Object> resultMap = adminService.clientRegistration(credentials);
            if(resultMap.get(Literals.STATUS).equals(Literals.TRUE)){
                return ResponseHandler.response(resultMap.get(Literals.RESPONSE), resultMap.get(Literals.MESSAGE).toString(), true, HttpStatus.OK);
            }
            return ResponseHandler.response(null, resultMap.get(Literals.MESSAGE).toString(), false, HttpStatus.BAD_REQUEST);

        }catch (Exception e){
            log.error(Literals.CATCH_EXCEPTION, e.getMessage());
            return ResponseHandler.response(null, Literals.SOMETHING_WENT_WRONG, false, HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Retrieve user details by ID", description = "Fetches detailed information of a user based on the provided userId.")
    @GetMapping(value = UrlMapping.GET_USER_BY_ID)
    public ResponseEntity<Object> getUserByUserId(@RequestParam(required = true) Long userId){
        try{
            Map<String, Object> resultMap = clientService.getUserDetailsByUserId(userId) ;
            if(resultMap.get(Literals.STATUS).equals(Literals.TRUE)){
                return ResponseHandler.response(resultMap.get(Literals.RESPONSE), resultMap.get(Literals.MESSAGE).toString(), true, HttpStatus.OK);
            }
            return ResponseHandler.response(null, resultMap.get(Literals.MESSAGE).toString(), false, HttpStatus.BAD_REQUEST);

        }catch (Exception e){
            log.error(Literals.CATCH_EXCEPTION, e.getMessage());
            return ResponseHandler.response(null, Literals.SOMETHING_WENT_WRONG, false, HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Retrieve all users", description = "Fetches a list of all registered users with their details.")
    @GetMapping(value = UrlMapping.GET_ALL_USERS)
    public ResponseEntity<Object> getAllUsers(){
        try{
            Map<String, Object> resultMap = clientService.getAllUsersDetails();
            if(resultMap.get(Literals.STATUS).equals(Literals.TRUE)){
                return ResponseHandler.response(resultMap.get(Literals.RESPONSE), resultMap.get(Literals.MESSAGE).toString(), true, HttpStatus.OK);
            }
            return ResponseHandler.response(null, resultMap.get(Literals.MESSAGE).toString(), false, HttpStatus.BAD_REQUEST);

        }catch (Exception e){
            log.error(Literals.CATCH_EXCEPTION, e.getMessage());
            return ResponseHandler.response(null, Literals.SOMETHING_WENT_WRONG, false, HttpStatus.BAD_REQUEST);
        }
    }


    //course enrollment
    @Operation(summary = "Enroll a user in a course", description = "Enrolls a user with the given userId into the specified course based on the provided courseId.")
    @PostMapping(value = UrlMapping.USER_COURSE_ENROLLMENT)
    public ResponseEntity<Object> courseEnrollment(@RequestParam(required = true) Long userId,
                                                   @RequestParam(required = true) Long courseId){
        try{
            Map<String, Object> resultMap = courseService.courseEnrollment(userId, courseId) ;
            if(resultMap.get(Literals.STATUS).equals(Literals.TRUE)){
                return ResponseHandler.response(resultMap.get(Literals.RESPONSE), resultMap.get(Literals.MESSAGE).toString(), true, HttpStatus.OK);
            }
            return ResponseHandler.response(null, resultMap.get(Literals.MESSAGE).toString(), false, HttpStatus.BAD_REQUEST);

        }catch (Exception e){
            log.error(Literals.CATCH_EXCEPTION, e.getMessage());
            return ResponseHandler.response(null, Literals.SOMETHING_WENT_WRONG, false, HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Update Enroll User in a course", description = "Update Enrolled user details with the given userId into the specified course based on the provided courseId.")
    @PutMapping(value = UrlMapping.UPDATE_USER_COURSE_ENROLLMENT)
    public ResponseEntity<Object> updateCourseEnrollment(@RequestParam(required = true) Long enrollmentId,
                                                         @RequestParam(required = true) Long userId,
                                                         @RequestParam(required = true) Long courseId){
        try{
            Map<String, Object> resultMap = courseService.updateEnrollUserForCourse(enrollmentId, userId, courseId) ;
            if(resultMap.get(Literals.STATUS).equals(Literals.TRUE)){
                return ResponseHandler.response(resultMap.get(Literals.RESPONSE), resultMap.get(Literals.MESSAGE).toString(), true, HttpStatus.OK);
            }
            return ResponseHandler.response(null, resultMap.get(Literals.MESSAGE).toString(), false, HttpStatus.BAD_REQUEST);

        }catch (Exception e){
            log.error(Literals.CATCH_EXCEPTION, e.getMessage());
            return ResponseHandler.response(null, Literals.SOMETHING_WENT_WRONG, false, HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Get Enroll User in a course by enroll id", description = "Get Enrolled user details with the given userId into the specified course based on the provided enrollmentId.")
    @GetMapping(value = UrlMapping.GET_USER_COURSE_ENROLLMENT_BY_ID)
    public ResponseEntity<Object> getUserCourseEnrollment(@RequestParam(required = true) Long enrollmentId){
        try{
            Map<String, Object> resultMap = courseService.getEnrollmentDetailsById(enrollmentId) ;
            if(resultMap.get(Literals.STATUS).equals(Literals.TRUE)){
                return ResponseHandler.response(resultMap.get(Literals.RESPONSE), resultMap.get(Literals.MESSAGE).toString(), true, HttpStatus.OK);
            }
            return ResponseHandler.response(null, resultMap.get(Literals.MESSAGE).toString(), false, HttpStatus.BAD_REQUEST);

        }catch (Exception e){
            log.error(Literals.CATCH_EXCEPTION, e.getMessage());
            return ResponseHandler.response(null, Literals.SOMETHING_WENT_WRONG, false, HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Get All Enroll User in a course with details" , description = "Get All Enrolled user details with user and  specified course details.")
    @GetMapping(value = UrlMapping.GET_ALL_USER_COURSE_ENROLLMENT)
    public ResponseEntity<Object> getAllUserCourseEnrollment(){
        try{
            Map<String, Object> resultMap = courseService.getAllEnrollmentDetails() ;
            if(resultMap.get(Literals.STATUS).equals(Literals.TRUE)){
                return ResponseHandler.response(resultMap.get(Literals.RESPONSE), resultMap.get(Literals.MESSAGE).toString(), true, HttpStatus.OK);
            }
            return ResponseHandler.response(null, resultMap.get(Literals.MESSAGE).toString(), false, HttpStatus.BAD_REQUEST);

        }catch (Exception e){
            log.error(Literals.CATCH_EXCEPTION, e.getMessage());
            return ResponseHandler.response(null, Literals.SOMETHING_WENT_WRONG, false, HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Remove User from Course Enrollment", description = "Remove the specified user's enrollment from a course and provide details of the removed user and course.")
    @DeleteMapping(value = UrlMapping.REMOVE_USER_COURSE_ENROLLMENT)
    public ResponseEntity<Object> removeUserCourseEnrollment(@RequestParam(required = true) Long enrollmentId){
        try{
            Map<String, Object> resultMap = courseService.removeEnrollUserForCourse(enrollmentId) ;
            if(resultMap.get(Literals.STATUS).equals(Literals.TRUE)){
                return ResponseHandler.response(resultMap.get(Literals.RESPONSE), resultMap.get(Literals.MESSAGE).toString(), true, HttpStatus.OK);
            }
            return ResponseHandler.response(null, resultMap.get(Literals.MESSAGE).toString(), false, HttpStatus.BAD_REQUEST);

        }catch (Exception e){
            log.error(Literals.CATCH_EXCEPTION, e.getMessage());
            return ResponseHandler.response(null, Literals.SOMETHING_WENT_WRONG, false, HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Unblock User", description = "Restores access for a specified user by unblocking their account.")
    @PutMapping(value = UrlMapping.UNBLOCK_USER)
    public ResponseEntity<Object> unblockUser(@RequestParam(required = true) Long userId){
        try{
            Map<String, Object> resultMap = adminService.unblockUser(userId) ;
            if(resultMap.get(Literals.STATUS).equals(Literals.TRUE)){
                return ResponseHandler.response(resultMap.get(Literals.RESPONSE), resultMap.get(Literals.MESSAGE).toString(), true, HttpStatus.OK);
            }
            return ResponseHandler.response(null, resultMap.get(Literals.MESSAGE).toString(), false, HttpStatus.BAD_REQUEST);

        }catch (Exception e){
            log.error(Literals.CATCH_EXCEPTION, e.getMessage());
            return ResponseHandler.response(null, Literals.SOMETHING_WENT_WRONG, false, HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Activate User Status", description = "Updates the activation status of a specified user.")
    @PutMapping(value = UrlMapping.ACTIVATE_USER_STATUS)
    public ResponseEntity<Object> setUserStatus(@RequestParam(required = true) Long userId,
                                                @RequestParam(required = true) boolean status){
        try{
            Map<String, Object> resultMap = adminService.setUserActivationStatus(userId, status) ;
            if(resultMap.get(Literals.STATUS).equals(Literals.TRUE)){
                return ResponseHandler.response(resultMap.get(Literals.RESPONSE), resultMap.get(Literals.MESSAGE).toString(), true, HttpStatus.OK);
            }
            return ResponseHandler.response(null, resultMap.get(Literals.MESSAGE).toString(), false, HttpStatus.BAD_REQUEST);

        }catch (Exception e){
            log.error(Literals.CATCH_EXCEPTION, e.getMessage());
            return ResponseHandler.response(null, Literals.SOMETHING_WENT_WRONG, false, HttpStatus.BAD_REQUEST);
        }
    }








}

