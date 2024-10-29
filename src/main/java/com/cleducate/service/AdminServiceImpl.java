package com.cleducate.service;

import com.cleducate.constants.Literals;
import com.cleducate.constants.MessageCode;
import com.cleducate.constants.UrlMapping;
import com.cleducate.dto.AdminDashboardDto;
import com.cleducate.dto.LoginRequestDto;
import com.cleducate.entity.ResetPasswordToken;
import com.cleducate.entity.Role;
import com.cleducate.entity.User;
import com.cleducate.repository.CourseRepository;
import com.cleducate.repository.ResetPasswordTokenRepository;
import com.cleducate.repository.RoleRepository;
import com.cleducate.repository.UserRepository;
import com.cleducate.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final EmailService emailService;
    private final JwtTokenProvider jwtTokenProvider;
    private final ResetPasswordTokenRepository tokenRepository;
    private final CourseRepository courseRepository;

    @Autowired
    public AdminServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository,
                            EmailService emailService, JwtTokenProvider jwtTokenProvider, ResetPasswordTokenRepository tokenRepository,
                            CourseRepository courseRepository){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.emailService = emailService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.tokenRepository = tokenRepository;
        this.courseRepository = courseRepository;
    }


    @Override
    public Map<String, Object> adminDashboard() {
        Map<String, Object> mapResult = new HashMap<>();
        mapResult.put(Literals.STATUS, Literals.FALSE);
        int totalUsers = userRepository.totalUsers();
        int totalActiveUsers = userRepository.totalActiveUsers();
        int totalCourses = courseRepository.totalCourses();
        int totalActiveCourses = courseRepository.totalActiveCourses();
        AdminDashboardDto response = new AdminDashboardDto(totalUsers, totalActiveUsers, totalCourses, totalActiveCourses);
        mapResult.put(Literals.RESPONSE, response);
        mapResult.put(Literals.STATUS, Literals.TRUE);
        mapResult.put(Literals.MESSAGE, MessageCode.ADMIN_DASHBOARD_DATA_FETCHED_SUCCESSFULLY);
        return mapResult;
    }

    @Override
    public Map<String, Object> clientRegistration(LoginRequestDto requestDto) {
        Map<String, Object> mapResult = new HashMap<>();
        mapResult.put(Literals.STATUS, Literals.FALSE);
        User user = new User();
        user.setEmail(requestDto.getEmail());
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        Role clientRole = roleRepository.findByRoleName(requestDto.getLoginBy());
        Set<Role> roles = new HashSet<>();
        roles.add(clientRole);
        user.setRoles(roles);
        user.setActive(true);
        user.setProfileCompleted(false);
        userRepository.save(user);
        emailService.sendEmailToClientOnRegistration(user, requestDto.getPassword());
        mapResult.put(Literals.RESPONSE, mapToClientResponse(user));
        mapResult.put(Literals.STATUS, Literals.TRUE);
        mapResult.put(Literals.MESSAGE, MessageCode.CLIENT_REGISTERED_SUCCESSFULLY);
        return mapResult;
    }

    public Map<String, Object> mapToClientResponse(User user) {
        Map<String, Object> map = new HashMap<>();
        map.put("email", user.getEmail());
        Set<String> roleNames = user.getRoles().stream()
                .map(role -> role.getRoleName().name())
                .collect(Collectors.toSet());
        map.put("roles", roleNames);
        map.put("isActive", user.isActive());
        map.put("isProfileCompleted", user.isProfileCompleted());
        return map;
    }


    @Override
    public Map<String, Object> unblockUser(Long userId) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put(Literals.STATUS, Literals.FALSE);
        Optional<User> user = userRepository.findByIdAndIsDeletedFalse(userId);
        if(!user.isPresent()){
            resultMap.put(Literals.MESSAGE, MessageCode.INVALID_USER_ID);
            return resultMap;
        }

        String resetToken = jwtTokenProvider.generatePasswordResetToken(user.get());
        ResetPasswordToken resetPasswordToken = new ResetPasswordToken(resetToken, user.get());
        tokenRepository.save(resetPasswordToken);
        String resetLink = UrlMapping.APPLICATION_URL + UrlMapping.RESET_PASSWORD + UrlMapping.TOKEN + resetToken;

        user.get().setResetPasswordAttempts(0);
        user.get().set_blocked(false);
        user.get().setActive(true);
        userRepository.save(user.get());

        emailService.sendEmailOnAccountUnBlock(user.get(), resetLink);
        resultMap.put(Literals.RESPONSE, Literals.SUCCESS);
        resultMap .put(Literals.STATUS, Literals.TRUE);
        resultMap.put(Literals.MESSAGE, MessageCode.USER_UNBLOCKED_SUCCESSFULLY);
        return resultMap;
    }

    @Override
    public Map<String, Object> setUserActivationStatus(Long userId, boolean status) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put(Literals.STATUS, Literals.FALSE);
        Optional<User> user = userRepository.findByIdAndIsDeletedFalse(userId);
        if(!user.isPresent()){
            resultMap.put(Literals.MESSAGE, MessageCode.INVALID_USER_ID);
            return resultMap;
        }
        user.get().setActive(status);
        userRepository.save(user.get());
        resultMap.put(Literals.RESPONSE, Literals.SUCCESS);
        resultMap .put(Literals.STATUS, Literals.TRUE);
        resultMap.put(Literals.MESSAGE, MessageCode.USER_ACTIVATED_SUCCESSFULLY);
        return resultMap;
    }
}
