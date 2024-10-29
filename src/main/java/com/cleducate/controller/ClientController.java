package com.cleducate.controller;

import com.cleducate.constants.Literals;
import com.cleducate.constants.UrlMapping;
import com.cleducate.dto.ClientProfileRequestDto;
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
public class ClientController {

    private final ClientService clientService;
    private final CourseService courseService;

    @Autowired
    public ClientController(ClientService clientService, CourseService courseService){
        this.clientService = clientService;
        this.courseService = courseService;
    }


    @Operation(summary = "Complete user profile", description = "Finalizes user profile setup with required details.")
    @PostMapping(value = UrlMapping.COMPLETE_PROFILE)
    public ResponseEntity<Object> completeClientProfile(@Valid @RequestBody ClientProfileRequestDto profileRequestDto){
        try{
            Map<String, Object> resultMap = clientService.completeProfile(profileRequestDto);
            if(resultMap.get(Literals.STATUS).equals(Literals.TRUE)){
                return ResponseHandler.response(resultMap.get(Literals.RESPONSE), resultMap.get(Literals.MESSAGE).toString(),true, HttpStatus.OK);
            }
            return ResponseHandler.response(null, resultMap.get(Literals.MESSAGE).toString(), false, HttpStatus.BAD_REQUEST);

        }catch (Exception e){
            log.error(Literals.CATCH_EXCEPTION, e.getMessage());
            return ResponseHandler.response(null, Literals.SOMETHING_WENT_WRONG, false, HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Fetch Assigned Courses for User", description = "Retrieves a list of courses assigned to the authenticated user, ensuring that the user has the CLIENT role.")
    @GetMapping(value = UrlMapping.ASSIGN_COURSES)
    public ResponseEntity<Object> assingCourses(){
        try{
            Map<String, Object> resultMap = clientService.getAssignCourses();
            if(resultMap.get(Literals.STATUS).equals(Literals.TRUE)){
                return ResponseHandler.response(resultMap.get(Literals.RESPONSE), resultMap.get(Literals.MESSAGE).toString(),true, HttpStatus.OK);
            }
            return ResponseHandler.response(null, resultMap.get(Literals.MESSAGE).toString(), false, HttpStatus.BAD_REQUEST);

        }catch (Exception e){
            log.error(Literals.CATCH_EXCEPTION, e.getMessage());
            return ResponseHandler.response(null, Literals.SOMETHING_WENT_WRONG, false, HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Get Course Details ", description = "Retrieve the information for a specific course using its unique ID.")
    @GetMapping(value = UrlMapping.GET_COURSE_DETAILS_BY_ID)
    public ResponseEntity<Object> getCourseById(@RequestParam Long courseId){
        try{
            Map<String, Object> resultMap = courseService.getCourseByCourseId(courseId);
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
