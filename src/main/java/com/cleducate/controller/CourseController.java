package com.cleducate.controller;

import com.cleducate.constants.AppConstants;
import com.cleducate.constants.Literals;
import com.cleducate.constants.UrlMapping;
import com.cleducate.dto.coursedto.CourseDto;
import com.cleducate.dto.coursedto.CourseResponseDto;
import com.cleducate.dto.coursedto.ModuleDto;
import com.cleducate.repository.CourseRepository;
import com.cleducate.service.CourseService;
import com.cleducate.utils.ResponseHandler;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class CourseController {

    private final CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService){
        this.courseService = courseService;
    }


    @Operation(summary = "Create a new course", description = "Creates a new course with details like title, description, and modules.")
    @PostMapping(value = UrlMapping.CREATE_COURSE)
    public ResponseEntity<Object> createCourse(@Valid @RequestBody CourseDto courseDto){
        try{
            Map<String, Object> resultMap = courseService.createCourse(courseDto);
            if(resultMap.get(Literals.STATUS).equals(Literals.TRUE)){
                return ResponseHandler.response(resultMap.get(Literals.RESPONSE), resultMap.get(Literals.MESSAGE).toString(), true, HttpStatus.OK);
            }
            return ResponseHandler.response(null, resultMap.get(Literals.MESSAGE).toString(), false, HttpStatus.BAD_REQUEST);

        }catch (Exception e){
            log.error(Literals.CATCH_EXCEPTION, e.getMessage());
            return ResponseHandler.response(null, Literals.SOMETHING_WENT_WRONG, false, HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Create multiple courses", description = "Creates multiple courses at once with their details.")
    @PostMapping(value = UrlMapping.CREATE_BULK_COURSES)
    public ResponseEntity<Object> createBulkCourses(@Valid @RequestBody List<CourseDto> courseDtoList){
        try{
            Map<String, Object> resultMap = courseService.createBulkCourses(courseDtoList);
            if(resultMap.get(Literals.STATUS).equals(Literals.TRUE)){
                return ResponseHandler.response(resultMap.get(Literals.RESPONSE), resultMap.get(Literals.MESSAGE).toString(), true, HttpStatus.OK);
            }
            return ResponseHandler.response(null, resultMap.get(Literals.MESSAGE).toString(), false, HttpStatus.BAD_REQUEST);

        }catch (Exception e){
            log.error(Literals.CATCH_EXCEPTION, e.getMessage());
            return ResponseHandler.response(null, Literals.SOMETHING_WENT_WRONG, false, HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Get Course Details", description = "Retrieve the information for a specific course using its unique ID.")
    @GetMapping(value = UrlMapping.GET_COURSE_BY_ID)
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

    @Operation(summary = "Retrieve all courses", description = "Fetches a list of all available courses.")
    @GetMapping(value = UrlMapping.GET_ALL_COURSES)
    public ResponseEntity<Object> getAllCourses(){
        try{
            Map<String, Object> resultMap = courseService.getAllCourses();
            if(resultMap.get(Literals.STATUS).equals(Literals.TRUE)){
                return ResponseHandler.response(resultMap.get(Literals.RESPONSE), resultMap.get(Literals.MESSAGE).toString(), true, HttpStatus.OK);
            }
            return ResponseHandler.response(null, resultMap.get(Literals.MESSAGE).toString(), false, HttpStatus.BAD_REQUEST);

        }catch (Exception e){
            log.error(Literals.CATCH_EXCEPTION, e.getMessage());
            return ResponseHandler.response(null, Literals.SOMETHING_WENT_WRONG, false, HttpStatus.BAD_REQUEST);
        }
    }


    @Operation(summary = "Retrieve all courses with details", description = "Fetches a list of all available courses with module details")
    @GetMapping(value = UrlMapping.GET_ALL_COURSES_WITH_DETAILS)
    public ResponseEntity<Object> getAllCoursesWithDetails(@RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer page,
                                                           @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_COUNT) Integer count,
                                                           @RequestParam(defaultValue = AppConstants.DEFAULT_SORT_BY) String sorting){
        try{
            Map<String, Object> resultMap = courseService.getAllCoursesWithDetails(page, count, sorting);
            if(resultMap.get(Literals.STATUS).equals(Literals.TRUE)){
                return ResponseHandler.response(resultMap.get(Literals.RESPONSE), resultMap.get(Literals.MESSAGE).toString(), true, HttpStatus.OK);
            }
            return ResponseHandler.response(null, resultMap.get(Literals.MESSAGE).toString(), false, HttpStatus.BAD_REQUEST);

        }catch (Exception e){
            log.error(Literals.CATCH_EXCEPTION, e.getMessage());
            return ResponseHandler.response(null, Literals.SOMETHING_WENT_WRONG, false, HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Update an existing course", description = "Updates the details of an existing course, including its title, description, and associated modules, based on the provided CourseResponseDto")
    @PutMapping(value = UrlMapping.UPDATE_COURSE)
    public ResponseEntity<Object> updateCourse(@RequestBody CourseResponseDto courseResponseDto){
        try{
            Map<String, Object> resultMap = courseService.updateCourse(courseResponseDto);
            if(resultMap.get(Literals.STATUS).equals(Literals.TRUE)){
                return ResponseHandler.response(resultMap.get(Literals.RESPONSE), resultMap.get(Literals.MESSAGE).toString(), true, HttpStatus.OK);
            }
            return ResponseHandler.response(null, resultMap.get(Literals.MESSAGE).toString(), false, HttpStatus.BAD_REQUEST);

        }catch (Exception e){
            log.error(Literals.CATCH_EXCEPTION, e.getMessage());
            return ResponseHandler.response(null, Literals.SOMETHING_WENT_WRONG, false, HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Remove an existing course", description = "Remove a course and its associated modules based on the provided course ID")
    @DeleteMapping(value = UrlMapping.REMOVE_COURSE)
    public ResponseEntity<Object> removeCourse(@RequestParam(required = true) Long courseId){
        try{
            Map<String, Object> resultMap = courseService.removeCourse(courseId);
            if(resultMap.get(Literals.STATUS).equals(Literals.TRUE)){
                return ResponseHandler.response(resultMap.get(Literals.RESPONSE), resultMap.get(Literals.MESSAGE).toString(), true, HttpStatus.OK);
            }
            return ResponseHandler.response(null, resultMap.get(Literals.MESSAGE).toString(), false, HttpStatus.BAD_REQUEST);

        }catch (Exception e){
            log.error(Literals.CATCH_EXCEPTION, e.getMessage());
            return ResponseHandler.response(null, Literals.SOMETHING_WENT_WRONG, false, HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Add a new module to a course", description = "Adds a new module to an existing course based on the provided CourseResponseDto")
    @PostMapping(value = UrlMapping.ADD_MODULE)
    public ResponseEntity<Object> addModule(@RequestBody ModuleDto moduleDto){
        try{
            Map<String, Object> resultMap = courseService.addModule(moduleDto);
            if(resultMap.get(Literals.STATUS).equals(Literals.TRUE)){
                return ResponseHandler.response(resultMap.get(Literals.RESPONSE), resultMap.get(Literals.MESSAGE).toString(), true, HttpStatus.OK);
            }
            return ResponseHandler.response(null, resultMap.get(Literals.MESSAGE).toString(), false, HttpStatus.BAD_REQUEST);

        }catch (Exception e){
            log.error(Literals.CATCH_EXCEPTION, e.getMessage());
            return ResponseHandler.response(null, Literals.SOMETHING_WENT_WRONG, false, HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Update an existing module", description = "Updates the details of an existing module, including its title, description, and other relevant information, based on the provided CourseResponseDto")
    @PutMapping(value = UrlMapping.UPDATE_MODULE)
    public ResponseEntity<Object> updateModule(@RequestBody ModuleDto moduleDto){
        try{
            Map<String, Object> resultMap = courseService.updateModule(moduleDto);
            if(resultMap.get(Literals.STATUS).equals(Literals.TRUE)){
                return ResponseHandler.response(resultMap.get(Literals.RESPONSE), resultMap.get(Literals.MESSAGE).toString(), true, HttpStatus.OK);
            }
            return ResponseHandler.response(null, resultMap.get(Literals.MESSAGE).toString(), false, HttpStatus.BAD_REQUEST);

        }catch (Exception e){
            log.error(Literals.CATCH_EXCEPTION, e.getMessage());
            return ResponseHandler.response(null, Literals.SOMETHING_WENT_WRONG, false, HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Remove an existing module", description = "Deletes an existing module from a course based on the provided module ID or details, ensuring all related information is handled appropriately.")
    @DeleteMapping(value = UrlMapping.REMOVE_MODULE)
    public ResponseEntity<Object> removeModule(@RequestParam(required = true) Long moduleId){
        try{
            Map<String, Object> resultMap = courseService.removeModule(moduleId);
            if(resultMap.get(Literals.STATUS).equals(Literals.TRUE)){
                return ResponseHandler.response(resultMap.get(Literals.RESPONSE), resultMap.get(Literals.MESSAGE).toString(), true, HttpStatus.OK);
            }
            return ResponseHandler.response(null, resultMap.get(Literals.MESSAGE).toString(), false, HttpStatus.BAD_REQUEST);

        }catch (Exception e){
            log.error(Literals.CATCH_EXCEPTION, e.getMessage());
            return ResponseHandler.response(null, Literals.SOMETHING_WENT_WRONG, false, HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Get Module Details", description = "Retrieve the information for a specific module using its unique ID.")
    @GetMapping(value = UrlMapping.GET_MODULE_BY_ID)
    public ResponseEntity<Object> getModuleById(@RequestParam(required = true) Long moduleId){
        try{
            Map<String, Object> resultMap = courseService.getModuleByModuleId(moduleId);
            if(resultMap.get(Literals.STATUS).equals(Literals.TRUE)){
                return ResponseHandler.response(resultMap.get(Literals.RESPONSE), resultMap.get(Literals.MESSAGE).toString(), true, HttpStatus.OK);
            }
            return ResponseHandler.response(null, resultMap.get(Literals.MESSAGE).toString(), false, HttpStatus.BAD_REQUEST);

        }catch (Exception e){
            log.error(Literals.CATCH_EXCEPTION, e.getMessage());
            return ResponseHandler.response(null, Literals.SOMETHING_WENT_WRONG, false, HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Retrieve all Modules", description = "Fetches a list of all available modules.")
    @GetMapping(value = UrlMapping.GET_ALL_MODULES)
    public ResponseEntity<Object> getAllModule(@RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_SIZE) Integer page,
                                               @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_COUNT) Integer count,
                                               @RequestParam(defaultValue = AppConstants.DEFAULT_SORT_BY) String sorting){
        try{
            Map<String, Object> resultMap = courseService.getAllModules(page, count, sorting);
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
