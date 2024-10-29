package com.cleducate.service;

import com.cleducate.constants.Literals;
import com.cleducate.constants.MessageCode;
import com.cleducate.dto.PageDTO;
import com.cleducate.dto.coursedto.CourseDto;
import com.cleducate.dto.coursedto.CourseResponseDto;
import com.cleducate.dto.coursedto.ModuleDto;
import com.cleducate.dto.coursedto.UserCourseEnrollmentDto;
import com.cleducate.entity.Course;
import com.cleducate.entity.Module;
import com.cleducate.entity.User;
import com.cleducate.entity.courseEnrollment.UserCourseEnrollment;
import com.cleducate.enums.CourseSorting;
import com.cleducate.enums.ModuleSoring;
import com.cleducate.enums.Roles;
import com.cleducate.repository.CourseRepository;
import com.cleducate.repository.ModuleRepository;
import com.cleducate.repository.UserCourseEnrollmentRepository;
import com.cleducate.repository.UserRepository;
import com.cleducate.utils.GenericUtils;
import com.cleducate.utils.ModelMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.jws.soap.SOAPBinding;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService{

    private final CourseRepository courseRepository;
    private final ModuleRepository moduleRepository;
    private final UserRepository userRepository;
    private final GenericUtils genericUtils;
    private final UserCourseEnrollmentRepository courseEnrollmentRepository;
    private final EmailService emailService;

    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository,
                            ModuleRepository moduleRepository,
                            UserRepository userRepository,
                             GenericUtils genericUtils,
                             UserCourseEnrollmentRepository courseEnrollmentRepository,
                             EmailService emailService){
        this.courseRepository = courseRepository;
        this.moduleRepository = moduleRepository;
        this.userRepository = userRepository;
        this.genericUtils = genericUtils;
        this.courseEnrollmentRepository = courseEnrollmentRepository;
        this.emailService = emailService;
    }

    @Override
    public Map<String, Object> createCourse(CourseDto courseDto) {

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put(Literals.STATUS, Literals.FALSE);
        if(Objects.isNull(courseDto)){
            resultMap.put(Literals.MESSAGE, MessageCode.COURSE_DATA_INVALID);
            return resultMap;
        }
        Course course = ModelMapperUtils.maptoEntity(courseDto, Course.class);

        if(courseDto.getModules() != null && !courseDto.getModules().isEmpty()){
            Set<Module> modules = courseDto.getModules()
                    .stream()
                    .map(moduleDto-> {
                        Module module = ModelMapperUtils.maptoEntity(moduleDto, Module.class);
                        module.setCourse(course);
                        return module;
                    }).collect(Collectors.toSet());
            course.setModules(modules);
        }
        courseRepository.save(course);
        resultMap.put(Literals.STATUS, Literals.TRUE);
        resultMap.put(Literals.RESPONSE, null);
        resultMap.put(Literals.MESSAGE, MessageCode.COURSE_CREATED);
        return resultMap;
    }

    @Override
    public Map<String, Object> createBulkCourses(List<CourseDto> courseDtoList) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put(Literals.STATUS, Literals.FALSE);
        if(Objects.isNull(courseDtoList) || courseDtoList.isEmpty()){
            resultMap.put(Literals.MESSAGE, MessageCode.COURSE_DATA_INVALID);
            return resultMap;
        }
        List<Map<String, Object>> createCoursesResponse = new ArrayList<>();

        for(CourseDto courseDto : courseDtoList){
            Map<String, Object> response = createCourse(courseDto);
            createCoursesResponse.add(response);
        }

        resultMap.put(Literals.STATUS, Literals.TRUE);
        resultMap.put(Literals.RESPONSE, createCoursesResponse);
        resultMap.put(Literals.MESSAGE, MessageCode.COURSES_CREATED);
        return resultMap;
    }

    @Override
    public Map<String, Object> getCourseByCourseId(Long courseId) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put(Literals.STATUS, Literals.FALSE);
        Course course = courseRepository.findCourseById(courseId);
        if(Objects.isNull(course)){
            resultMap.put(Literals.MESSAGE, MessageCode.INVALID_COURSE_ID);
            return resultMap;
        }
        CourseDto courseDto = ModelMapperUtils.map(course, CourseDto.class);
        resultMap.put(Literals.STATUS, Literals.TRUE);
        resultMap.put(Literals.RESPONSE, courseDto);
        resultMap.put(Literals.MESSAGE, MessageCode.COURSES_CREATED);
        return resultMap;
    }

    @Override
    public Map<String, Object> getAllCoursesWithDetails(Integer page, Integer count, String sorting) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put(Literals.STATUS, Literals.FALSE);

        if(Arrays.stream(CourseSorting.values()).noneMatch((e)->e.name().equalsIgnoreCase(sorting))){
            resultMap.put(Literals.MESSAGE, MessageCode.INVALID_COURSE_SORTING);
            return resultMap;
        }

        Pageable pageable = getCoursePageableAndSorting(page, count, sorting);
        Page<Course> coursePage = courseRepository.getAllCoursesWithDetails(pageable);

        if(Objects.isNull(coursePage) || coursePage.isEmpty()){
            resultMap.put(Literals.MESSAGE, MessageCode.COURSE_NOT_FOUND);
            return resultMap;
        }

        List<CourseDto> response = coursePage.stream()
                .map(course -> ModelMapperUtils.map(course, CourseDto.class))
                .collect(Collectors.toList());

        PageDTO pageDTO = new PageDTO(
                response, coursePage.getNumberOfElements(), coursePage.getTotalPages(),
                coursePage.getTotalElements(), coursePage.isLast()
        );

        resultMap.put(Literals.RESPONSE, pageDTO);
        resultMap.put(Literals.STATUS, Literals.TRUE);
        resultMap.put(Literals.MESSAGE, MessageCode.COURSES_CREATED);
        return resultMap;
    }

    private Pageable getCoursePageableAndSorting(Integer page, Integer count, String sorting){
        Sort sort ;
        String normalizeSorting = Optional.ofNullable(sorting).map(String :: toUpperCase).orElse("UNSORTED");

        switch (normalizeSorting){
            case "ID" :
                sort = Sort.by(CourseSorting.ID.name()).ascending();
                break;
            case "TITLE":
                sort = Sort.by(CourseSorting.TITLE.name()).ascending();
                break;
            case "REFERENCE_CODE":
                sort = Sort.by(CourseSorting.REFERENCE_CODE.name()).ascending();
                break;
            case "IS_DELETED" :
                sort = Sort.by(CourseSorting.IS_DELETED.name()).ascending();
                break;
            case "IS_REMOVED":
                sort = Sort.by(CourseSorting.IS_REMOVED.name()).ascending();
                break;
            case "IS_ACTIVE":
                sort = Sort.by(CourseSorting.IS_ACTIVE.name()).ascending();
                break;
            default:
                sort = Sort.unsorted();
                break;
        }
        return PageRequest.of(page, count, sort);
    }

    @Override
    public Map<String, Object> getAllCourses() {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put(Literals.STATUS, Literals.FALSE);
        List<Course> courseList = courseRepository.getAllCourses();
        if(Objects.isNull(courseList) || courseList.isEmpty()){
            resultMap.put(Literals.MESSAGE, MessageCode.COURSE_NOT_FOUND);
            return resultMap;
        }
        List<CourseResponseDto> courseDtoList = courseList.stream()
                .map(course -> ModelMapperUtils.map(course, CourseResponseDto.class))
                .collect(Collectors.toList());

        resultMap.put(Literals.STATUS, Literals.TRUE);
        resultMap.put(Literals.RESPONSE, courseDtoList);
        resultMap.put(Literals.MESSAGE, MessageCode.COURSES_CREATED);
        return resultMap;
    }

    @Override
    public Map<String, Object> updateCourse(CourseResponseDto responseDto) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put(Literals.STATUS, Literals.FALSE);
        Course course = courseRepository.findCourseById(responseDto.getId());
        if(Objects.isNull(course)){
            resultMap.put(Literals.MESSAGE, MessageCode.INVALID_COURSE_ID);
            return resultMap;
        }
        course.setTitle(responseDto.getTitle());
        course.setDescription(responseDto.getDescription());
        course.setReferenceCode(responseDto.getReferenceCode());
        course.setThumbnailUrl(responseDto.getThumbnailUrl());
        course.setActive(responseDto.isActive());
        course = courseRepository.save(course);
        CourseResponseDto response = ModelMapperUtils.map(course, CourseResponseDto.class);
        resultMap.put(Literals.STATUS, Literals.TRUE);
        resultMap.put(Literals.RESPONSE, response);
        resultMap.put(Literals.MESSAGE, MessageCode.COURSE_UPDATED);
        return resultMap;
    }

    @Override
    public Map<String, Object> removeCourse(Long courseId) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put(Literals.STATUS, Literals.FALSE);
        Course course = courseRepository.findCourseById(courseId);
        if(Objects.isNull(course)){
            resultMap.put(Literals.MESSAGE, MessageCode.INVALID_COURSE_ID);
            return resultMap;
        }
        if(course.getModules() != null && !course.getModules().isEmpty()){
            course.getModules()
                    .forEach(module ->{
                        module.setRemoved(true);
                        module.setActive(false);
                        module.setDeleted(true);
                        moduleRepository.save(module);
                    });
        }
        course.setActive(false);
        course.setRemoved(true);
        course.setDeleted(true);
        course = courseRepository.save(course);
        CourseResponseDto response = ModelMapperUtils.map(course, CourseResponseDto.class);
        resultMap.put(Literals.STATUS, Literals.TRUE);
        resultMap.put(Literals.RESPONSE, response);
        resultMap.put(Literals.MESSAGE, MessageCode.COURSE_REMOVED);
        return resultMap;
    }

    @Override
    public Map<String, Object> addModule(ModuleDto moduleDto) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put(Literals.STATUS, Literals.FALSE);
        Course course = courseRepository.findCourseById(moduleDto.getCourseId());
        if(Objects.isNull(course)){
            resultMap.put(Literals.MESSAGE, MessageCode.INVALID_COURSE_ID);
            return resultMap;
        }
        Module module = Module.builder()
                .title(moduleDto.getTitle())
                .description(moduleDto.getDescription())
                .thumbnailUrl(moduleDto.getThumbnailUrl())
                .lectureUrl(moduleDto.getLectureUrl())
                .code(moduleDto.getCode())
                .isActive(moduleDto.isActive())
                .course(course)
                .build();

        if (course.getModules() == null) {
            course.setModules(new HashSet<>());
        }

        course.getModules().add(module);
        courseRepository.save(course);
        moduleRepository.save(module);
        ModuleDto response = ModelMapperUtils.map(module, ModuleDto.class);
        resultMap.put(Literals.STATUS, Literals.TRUE);
        resultMap.put(Literals.RESPONSE, response);
        resultMap.put(Literals.MESSAGE, MessageCode.MODULE_ADDED);
        return resultMap;
    }

    @Override
    public Map<String, Object> updateModule(ModuleDto moduleDto) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put(Literals.STATUS, Literals.FALSE);
        Course course = courseRepository.findCourseById(moduleDto.getCourseId());
        Module module = moduleRepository.getModuleById(moduleDto.getId());
        if(Objects.isNull(course)){
            resultMap.put(Literals.MESSAGE, MessageCode.INVALID_COURSE_ID);
            return resultMap;
        }
        if(Objects.isNull(module)){
            resultMap.put(Literals.MESSAGE, MessageCode.INVALID_MODULE_ID);
            return resultMap;
        }
        module.setTitle(moduleDto.getTitle());
        module.setDescription(moduleDto.getDescription());
        module.setCode(moduleDto.getCode());
        module.setThumbnailUrl(moduleDto.getThumbnailUrl());
        module.setLectureUrl(moduleDto.getLectureUrl());
        module.setActive(moduleDto.isActive());
        module.setCourse(course);
        module = moduleRepository.save(module);
        ModuleDto response = ModelMapperUtils.map(module, ModuleDto.class);
        resultMap.put(Literals.STATUS, Literals.TRUE);
        resultMap.put(Literals.RESPONSE, response);
        resultMap.put(Literals.MESSAGE, MessageCode.MODULE_UPDATED);
        return resultMap;
    }

    @Override
    public Map<String, Object> removeModule(Long moduleId) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put(Literals.STATUS, Literals.FALSE);
        Module module = moduleRepository.getModuleById(moduleId);
        if(Objects.isNull(module)){
            resultMap.put(Literals.MESSAGE, MessageCode.INVALID_MODULE_ID);
            return resultMap;
        }
        module.setActive(false);
        module.setDeleted(true);
        module.setRemoved(true);
        module = moduleRepository.save(module);
        ModuleDto response = ModelMapperUtils.map(module, ModuleDto.class);
        resultMap.put(Literals.STATUS, Literals.TRUE);
        resultMap.put(Literals.RESPONSE, response);
        resultMap.put(Literals.MESSAGE, MessageCode.MODULE_REMOVED);
        return resultMap;
    }


    @Override
    public Map<String, Object> getModuleByModuleId(Long moduleId) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put(Literals.STATUS, Literals.FALSE);
        Module module = moduleRepository.getModuleById(moduleId);
        if(Objects.isNull(module)){
            resultMap.put(Literals.MESSAGE, MessageCode.INVALID_MODULE_ID);
            return resultMap;
        }
        ModuleDto response = ModelMapperUtils.map(module, ModuleDto.class);
        response.setCourseId(module.getCourse().getId());
        resultMap.put(Literals.STATUS, Literals.TRUE);
        resultMap.put(Literals.RESPONSE, response);
        resultMap.put(Literals.MESSAGE, MessageCode.MODULE_REMOVED);
        return resultMap;
    }

    @Override
    public Map<String, Object> getAllModules(Integer page, Integer count, String sorting) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put(Literals.STATUS, Literals.FALSE);

        if(Arrays.stream(ModuleSoring.values()).noneMatch((e)->e.name().equalsIgnoreCase(sorting))){
            resultMap.put(Literals.MESSAGE, MessageCode.INVALID_MODULE_SORTING);
            return resultMap;
        }

        Pageable pageable = getModulePageableAndSorting(page, count, sorting);
        Page<Module> modulePage = moduleRepository.getAllModules(pageable);

        if(Objects.isNull(modulePage) || modulePage.isEmpty()){
            resultMap.put(Literals.MESSAGE, MessageCode.MODULE_NOT_FOUND);
            return resultMap;
        }

        List<ModuleDto> moduleDtoList = modulePage
                .stream()
                .map(module ->{
                    ModuleDto moduleDto = ModelMapperUtils.map(module, ModuleDto.class);
                    if(module.getCourse() != null){
                        moduleDto.setCourseId(module.getCourse().getId());
                    }
                    return moduleDto;
                })
                .collect(Collectors.toList());

        PageDTO response = new PageDTO(moduleDtoList, modulePage.getNumberOfElements(),
                modulePage.getTotalPages(), modulePage.getTotalElements(), modulePage.isLast()
        );

        resultMap.put(Literals.STATUS, Literals.TRUE);
        resultMap.put(Literals.RESPONSE, response);
        resultMap.put(Literals.MESSAGE, MessageCode.MODULE_REMOVED);
        return resultMap;
    }

    private Pageable getModulePageableAndSorting(Integer page, Integer count, String sorting){
        Sort sort;
        String normalizeSorting = Optional.ofNullable(sorting).map(String :: toUpperCase).orElse("UNSORTED");
        switch (normalizeSorting){
            case "ID" :
                sort = Sort.by(ModuleSoring.ID.name()).ascending();
                break;
            case "TITLE":
                sort = Sort.by(ModuleSoring.TITLE.name()).ascending();
                break;
            case "CODE":
                sort = Sort.by(ModuleSoring.CODE.name()).ascending();
                break;
            case "IS_ACTIVE":
                sort = Sort.by(ModuleSoring.IS_ACTIVE.name()).ascending();
                break;
            case "IS_DELETED" :
                sort = Sort.by(ModuleSoring.IS_DELETED.name()).ascending();
                break;
            case "IS_REMOVED":
                sort = Sort.by(ModuleSoring.IS_REMOVED.name()).ascending();
                break;
            default:
                sort = Sort.unsorted();
                break;

        }
        return PageRequest.of(page, count, sort);
    }

    @Override
    public Map<String, Object> courseEnrollment(Long userId, Long courseId) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put(Literals.STATUS, Literals.FALSE);

        if(courseEnrollmentRepository.isUserIsAlreadyEnrolledInCourse(userId, courseId)){
            resultMap.put(Literals.MESSAGE, MessageCode.USER_ALREADY_ENROLLED_IN_COURSE);
            return resultMap;
        }

        User admin = genericUtils.getLoggedInUser();
        if(admin.getRoles().stream().noneMatch(role -> Roles.SUPER_ADMIN.name().equals(role.getRoleName().name()))){
            resultMap.put(Literals.MESSAGE, MessageCode.ACCESS_DENIED);
            return resultMap;
        }
        Optional<User> user = userRepository.findById(userId);
        if(!user.isPresent()){
            resultMap.put(Literals.MESSAGE, MessageCode.INVALID_USER_ID);
            return resultMap;
        }
        if(user.get().getRoles().stream().noneMatch(role -> Roles.CLIENT.name().equals(role.getRoleName().name()))){
            resultMap.put(Literals.MESSAGE, MessageCode.INVALID_USER_ROLE);
            return resultMap;
        }
        Course course = courseRepository.findCourseById(courseId);
        if(Objects.isNull(course)){
            resultMap.put(Literals.MESSAGE, MessageCode.INVALID_COURSE_ID);
            return resultMap;
        }

        UserCourseEnrollment userCourseEnrollment = UserCourseEnrollment.builder()
                .admin(admin)
                .user(user.get())
                .course(course)
                .assignmentDate(ZonedDateTime.now())
                .isActive(true)
                .isCompleted(false)
                .isRemoved(false)
                .build();
        userCourseEnrollment=courseEnrollmentRepository.save(userCourseEnrollment);

        Map<String, Object> responseDto = mapUserCourseEnrollmentResponse(userCourseEnrollment);

        emailService.sendEmailToAdminOnCourseEnrollment(userCourseEnrollment);
        emailService.sendEmailToClientOnCourseEnrollment(userCourseEnrollment);

        resultMap.put(Literals.STATUS, Literals.TRUE);
        resultMap.put(Literals.RESPONSE, responseDto);
        resultMap.put(Literals.MESSAGE, MessageCode.USER_SUCCESSFULLY_ENROLLED);
        return resultMap;
    }

    private Map<String, Object> mapUserCourseEnrollmentResponse(UserCourseEnrollment userCourseEnrollment) {
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("id", userCourseEnrollment.getId());
        responseMap.put("adminId", userCourseEnrollment.getAdmin().getId());
        responseMap.put("userId", userCourseEnrollment.getUser().getId());
        responseMap.put("userName", userCourseEnrollment.getUser().getFirstName()+" "+userCourseEnrollment.getUser().getLastName());
        responseMap.put("courseId", userCourseEnrollment.getCourse().getId());
        responseMap.put("assignmentDate", userCourseEnrollment.getAssignmentDate());
        responseMap.put("completionDate", userCourseEnrollment.getCompletionDate());
        responseMap.put("isActive", userCourseEnrollment.isActive());
        responseMap.put("isRemoved", userCourseEnrollment.isRemoved());
        responseMap.put("progressPercentage", userCourseEnrollment.getProgressPercentage());
        responseMap.put("isCompleted", userCourseEnrollment.isCompleted());
        return responseMap;
    }

    @Override
    public Map<String, Object> getEnrollmentDetailsById(Long enrollmentId) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put(Literals.STATUS, Literals.FALSE);

        UserCourseEnrollment userCourseEnrollment = courseEnrollmentRepository.findEnrollmentDetailsById(enrollmentId);
        if(Objects.isNull(userCourseEnrollment)){
            resultMap.put(Literals.MESSAGE, MessageCode.INVALID_ENROLLMENT_ID);
            return resultMap;
        }
        Map<String, Object> responseDto = mapUserCourseEnrollmentResponse(userCourseEnrollment);
        resultMap.put(Literals.STATUS, Literals.TRUE);
        resultMap.put(Literals.RESPONSE, responseDto);
        resultMap.put(Literals.MESSAGE, MessageCode.USER_SUCCESSFULLY_ENROLLED);
        return resultMap;
    }

    @Override
    public Map<String, Object> getAllEnrollmentDetails() {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put(Literals.STATUS, Literals.FALSE);

        List<UserCourseEnrollment> userCourseEnrollmentList = courseEnrollmentRepository.getAllUserCourseEnrollmentDetails();
        if(Objects.isNull(userCourseEnrollmentList) || userCourseEnrollmentList.isEmpty()){
            resultMap.put(Literals.MESSAGE, MessageCode.ENROLLMENT_DETAILS_NOT_FOUND);
            return resultMap;
        }
        List<Map<String, Object>>  courseEnrollmentResponses = userCourseEnrollmentList
                .stream()
                .map(this :: mapUserCourseEnrollmentResponse)
                .collect(Collectors.toList());

        resultMap.put(Literals.STATUS, Literals.TRUE);
        resultMap.put(Literals.RESPONSE, courseEnrollmentResponses);
        resultMap.put(Literals.MESSAGE, MessageCode.USER_SUCCESSFULLY_ENROLLED);
        return resultMap;
    }

    @Override
    public Map<String, Object> updateEnrollUserForCourse(Long enrollmentId, Long userId, Long courseId) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put(Literals.STATUS, Literals.FALSE);

        UserCourseEnrollment userCourseEnrollment = courseEnrollmentRepository.findEnrollmentDetailsById(enrollmentId);
        if(Objects.isNull(userCourseEnrollment)){
            resultMap.put(Literals.MESSAGE, MessageCode.INVALID_ENROLLMENT_ID);
            return resultMap;
        }

        if(!courseEnrollmentRepository.isUserIsAlreadyEnrolledInCourse(userId, courseId)){
            resultMap.put(Literals.MESSAGE, MessageCode.USER_NOT_ENROLLED_IN_COURSE);
            return resultMap;
        }

        User admin = genericUtils.getLoggedInUser();
        if(admin.getRoles().stream().noneMatch(role -> Roles.SUPER_ADMIN.name().equals(role.getRoleName().name()))){
            resultMap.put(Literals.MESSAGE, MessageCode.ACCESS_DENIED);
            return resultMap;
        }
        Optional<User> user = userRepository.findById(userId);
        if(!user.isPresent()){
            resultMap.put(Literals.MESSAGE, MessageCode.INVALID_USER_ID);
            return resultMap;
        }
        if(user.get().getRoles().stream().noneMatch(role -> Roles.CLIENT.name().equals(role.getRoleName().name()))){
            resultMap.put(Literals.MESSAGE, MessageCode.INVALID_USER_ROLE);
            return resultMap;
        }
        Course course = courseRepository.findCourseById(courseId);
        if(Objects.isNull(course)){
            resultMap.put(Literals.MESSAGE, MessageCode.INVALID_COURSE_ID);
            return resultMap;
        }

        userCourseEnrollment.setUser(user.get());
        userCourseEnrollment.setAdmin(admin);
        userCourseEnrollment.setCourse(course);
        userCourseEnrollment=courseEnrollmentRepository.save(userCourseEnrollment);

        Map<String, Object> responseDto = mapUserCourseEnrollmentResponse(userCourseEnrollment);

        resultMap.put(Literals.STATUS, Literals.TRUE);
        resultMap.put(Literals.RESPONSE, responseDto);
        resultMap.put(Literals.MESSAGE, MessageCode.USER_SUCCESSFULLY_ENROLLED);
        return resultMap;
    }

    @Override
    public Map<String, Object> removeEnrollUserForCourse(Long enrollmentId) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put(Literals.STATUS, Literals.FALSE);

        UserCourseEnrollment userCourseEnrollment = courseEnrollmentRepository.findEnrollmentDetailsById(enrollmentId);
        if(Objects.isNull(userCourseEnrollment)){
            resultMap.put(Literals.MESSAGE, MessageCode.INVALID_ENROLLMENT_ID);
            return resultMap;
        }

        userCourseEnrollment.setActive(false);
        userCourseEnrollment.setDeleted(true);
        userCourseEnrollment.setRemoved(true);
        courseEnrollmentRepository.save(userCourseEnrollment);

        Map<String, Object> responseDto = mapUserCourseEnrollmentResponse(userCourseEnrollment);

        resultMap.put(Literals.STATUS, Literals.TRUE);
        resultMap.put(Literals.RESPONSE, responseDto);
        resultMap.put(Literals.MESSAGE, MessageCode.USER_SUCCESSFULLY_ENROLLED);
        return resultMap;
    }
}


