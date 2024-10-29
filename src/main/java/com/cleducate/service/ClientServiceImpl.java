package com.cleducate.service;

import com.cleducate.constants.Literals;
import com.cleducate.constants.MessageCode;
import com.cleducate.constants.UrlMapping;
import com.cleducate.dto.AddressDto;
import com.cleducate.dto.ClientProfileRequestDto;
import com.cleducate.dto.RoleDto;
import com.cleducate.dto.UserDto;
import com.cleducate.dto.coursedto.CourseDto;
import com.cleducate.dto.coursedto.CourseResponseDto;
import com.cleducate.entity.Address;
import com.cleducate.entity.Course;
import com.cleducate.entity.Role;
import com.cleducate.entity.User;
import com.cleducate.entity.courseEnrollment.UserCourseEnrollment;
import com.cleducate.enums.CurrentStatus;
import com.cleducate.enums.Roles;
import com.cleducate.repository.AddressRepository;
import com.cleducate.repository.CourseRepository;
import com.cleducate.repository.UserCourseEnrollmentRepository;
import com.cleducate.repository.UserRepository;
import com.cleducate.utils.GenericUtils;
import com.cleducate.utils.ModelMapperUtils;
import com.cleducate.utils.ResponseHandler;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.method.annotation.ModelMethodProcessor;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ClientServiceImpl implements ClientService{

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final EmailService emailService;
    private final GenericUtils genericUtils;
    private final UserCourseEnrollmentRepository courseEnrollmentRepository;
    private final CourseRepository courseRepository;


    @Autowired
    public ClientServiceImpl(UserRepository userRepository, AddressRepository addressRepository,
                             EmailService emailService, GenericUtils genericUtils, UserCourseEnrollmentRepository courseEnrollmentRepository,
                             CourseRepository courseRepository ){
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
        this.emailService = emailService;
        this.genericUtils = genericUtils;
        this.courseEnrollmentRepository = courseEnrollmentRepository;
        this.courseRepository = courseRepository;
    }

    @Override
    public Map<String, Object> completeProfile(ClientProfileRequestDto profileRequestDto) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put(Literals.STATUS, Literals.FALSE);
        User user = userRepository.findByEmail(profileRequestDto.getEmail());

        if(Objects.isNull(user)){
            resultMap.put(Literals.MESSAGE, MessageCode.USER_ACCOUNT_NOT_FOUND);
            return resultMap;
        }

        if(user.isProfileCompleted()){
            resultMap.put(Literals.MESSAGE, MessageCode.CLIENT_EMAIL_DOES_NOT_EXIST);
            return resultMap;
        }
        user.setFirstName(profileRequestDto.getFirstName());
        user.setLastName(profileRequestDto.getLastName());
        user.setPhone(profileRequestDto.getPhone());
        user.setGender(profileRequestDto.getGender());
        Address address = ModelMapperUtils.maptoEntity(profileRequestDto.getAddress(), Address.class);
        addressRepository.save(address);
        user.setAddress(address);
        user.setCurrentStatus(CurrentStatus.ACTIVE);
        user.setProfileCompleted(true);
        user = userRepository.save(user);
        emailService.sendEmailOnProfileCompletion(user);
        resultMap.put(Literals.RESPONSE, mapToUserDto(user));
        resultMap.put(Literals.MESSAGE, MessageCode.CLIENT_PROFILE_CREATED);
        resultMap.put(Literals.STATUS, Literals.TRUE);
        return resultMap;
    }

    public  Map<String, Object> mapToUserDto(User user){
        Map<String, Object> map = new HashMap<>();
        map.put("id", user.getId());
        map.put("firstName", user.getFirstName());
        map.put("lastName", user.getLastName());
        map.put("email", user.getEmail());
        map.put("phone", user.getPhone());
        map.put("gender", user.getGender());
        if(user.getAddress() != null){
            AddressDto addressDto = new AddressDto();
            Address address = user.getAddress();
            addressDto.setCity(address.getCity());
            addressDto.setStateProvince(address.getStateProvince());
            addressDto.setCountry(address.getCountry());
            addressDto.setZipCode(address.getZipCode());
            map.put("address", addressDto);
        }
        map.put("isProfileCompleted", user.isProfileCompleted());
        map.put("currentStatus", user.getCurrentStatus());
        return map;
    }

    @Override
    public Map<String, Object> getUserDetailsByUserId(Long userId) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put(Literals.STATUS, Literals.FALSE);
        Optional<User> user = userRepository.findByIdAndIsDeletedFalse(userId);

        if(Objects.isNull(user)){
            resultMap.put(Literals.MESSAGE, MessageCode.INVALID_USER_ID);
            return resultMap;
        }

        UserDto userDto = ModelMapperUtils.map(user, UserDto.class);
        if (userDto.getAddress() != null) {
            AddressDto addressDto = ModelMapperUtils.map(user.get().getAddress(), AddressDto.class);
            userDto.setAddress(addressDto);
        } else {
            userDto.setAddress(null);
        }
        Roles roles = user.get().getRoles().stream().map(Role :: getRoleName).findFirst().orElse(null);
        userDto.setRoles(roles);
        resultMap.put(Literals.RESPONSE, userDto);
        resultMap.put(Literals.MESSAGE, MessageCode.USER_DETAILS_FETCH_SUCCESSFULLY);
        resultMap.put(Literals.STATUS, Literals.TRUE);
        return resultMap;
    }

    @Override
    public Map<String, Object> getAllUsersDetails() {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put(Literals.STATUS, Literals.FALSE);
        List<User> userList = userRepository.getAllUsers();

        if(Objects.isNull(userList) || userList.isEmpty()){
            resultMap.put(Literals.MESSAGE, MessageCode.USER_ACCOUNT_NOT_FOUND);
            return resultMap;
        }

        List<UserDto> userDtoList = userList.stream()
                .map((user) -> {
                    UserDto userDto = ModelMapperUtils.map(user, UserDto.class);
                    if (user.getAddress() != null) {
                        AddressDto addressDto = ModelMapperUtils.map(user.getAddress(), AddressDto.class);
                        userDto.setAddress(addressDto);
                    } else {
                        userDto.setAddress(null);  // Ensure the address is set as null if not present
                    }
                    Roles role = user.getRoles().stream().map(Role :: getRoleName).findFirst().orElse(null);
                    userDto.setRoles(role);
                    return userDto;
                })
                .collect(Collectors.toList());

        resultMap.put(Literals.RESPONSE, userDtoList);
        resultMap.put(Literals.MESSAGE, MessageCode.USER_DETAILS_FETCH_SUCCESSFULLY);
        resultMap.put(Literals.STATUS, Literals.TRUE);
        return resultMap;
    }

    @Override
    public Map<String, Object> getAssignCourses() {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put(Literals.STATUS, Literals.FALSE);
        User user = genericUtils.getLoggedInUser();
        if(user.getRoles().stream().noneMatch((role -> Roles.CLIENT.name().equals(role.getRoleName().name())))){
            resultMap.put(Literals.MESSAGE, MessageCode.INVALID_USER_ROLE);
            return resultMap;
        }
        if(!courseEnrollmentRepository.isUserExistInEnrolledCourses(user.getId())){
            resultMap.put(Literals.MESSAGE, MessageCode.USER_COURSE_NOT_FOUND);
            return resultMap;
        }
        List<UserCourseEnrollment> courseEnrollmentList = courseEnrollmentRepository.getAllUserCourseEnrollmentDetailsByUserId(user.getId());
        if(Objects.isNull(courseEnrollmentList) || courseEnrollmentList.isEmpty()){
            resultMap.put(Literals.MESSAGE, MessageCode.ENROLLMENT_DETAILS_NOT_FOUND);
            return resultMap;
        }
        List<CourseResponseDto> courseList = courseEnrollmentList
                .stream()
                .map(userCourseEnrollment -> {
                    Course course = courseRepository.findCourseById(userCourseEnrollment.getCourse().getId());
                    return ModelMapperUtils.map(course, CourseResponseDto.class);
                        })
                .collect(Collectors.toList());

        resultMap.put(Literals.RESPONSE, courseList );
        resultMap.put(Literals.MESSAGE, MessageCode.COURSE_FETCHED_SUCCESSFULLY);
        resultMap.put(Literals.STATUS, Literals.TRUE);
        return resultMap;
    }


}
