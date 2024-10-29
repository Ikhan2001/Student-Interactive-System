package com.cleducate.service;

import com.cleducate.constants.AppConstants;
import com.cleducate.constants.Literals;
import com.cleducate.constants.MessageCode;
import com.cleducate.constants.UrlMapping;
import com.cleducate.dto.LoginRequestDto;
import com.cleducate.dto.SessionDto;
import com.cleducate.entity.ResetPasswordToken;
import com.cleducate.entity.Session;
import com.cleducate.entity.User;
import com.cleducate.repository.ResetPasswordTokenRepository;
import com.cleducate.repository.UserRepository;
import com.cleducate.security.JwtTokenProvider;
import com.cleducate.utils.DateUtils;
import com.cleducate.utils.ModelMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Email;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private static final int MAX_ATTEMPTS = 3;

    private final UserRepository userRepository ;
    private final SessionService sessionService;
    private final ResetPasswordTokenRepository resetPasswordTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthenticationServiceImpl(UserRepository userRepository, SessionService sessionService,
                                     ResetPasswordTokenRepository resetPasswordTokenRepository,
                                     JwtTokenProvider jwtTokenProvider, EmailService emailService,
                                     PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.sessionService = sessionService;
        this.resetPasswordTokenRepository = resetPasswordTokenRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Map<String, Object> authenticateUserAccount(LoginRequestDto credentials) {
        Map<String, Object> mapResult = new HashMap<>();
        mapResult.put(Literals.STATUS, Literals.FALSE);
        User user = userRepository.findByEmailIgnoreCaseAndRoles_RoleName(credentials.getEmail().trim(), credentials.getLoginBy());
        if(Objects.isNull(user)){
            mapResult.put(Literals.MESSAGE, MessageCode.USER_ACCOUNT_NOT_FOUND);
            return mapResult;
        }
        if(user.isDeleted()){
            mapResult.put(Literals.MESSAGE, MessageCode.USER_DELETED);
            return mapResult;
        }
        if(!user.isActive()){
            mapResult.put(Literals.MESSAGE, MessageCode.USER_DISABLED);
            return mapResult;
        }
        if(!user.isProfileCompleted()){
            mapResult.put(Literals.MESSAGE, MessageCode.CLIENT_PROFILE_INCOMPLETE);
            return mapResult;
        }
        Map<String, Object> response = new HashMap<>();
        response.put(Literals.DATA, null);
        if(user.getPassword() != null && BCrypt.checkpw(credentials.getPassword(), user.getPassword())){
            if(!DateUtils.isValidTimeZone(credentials.getTimeZone())){
                mapResult.put(Literals.MESSAGE, MessageCode.INVALID_TIMEZONE);
                return mapResult;
            }
            user.setTimezone(credentials.getTimeZone());
            userRepository.save(user);
            Session session = sessionService.createSession(user, credentials);
            SessionDto dto = ModelMapperUtils.map(session, SessionDto.class);
            response.put(Literals.DATA, dto);

            mapResult.put(Literals.RESPONSE, response);
            mapResult.put(Literals.STATUS, Literals.TRUE);
            mapResult.put(Literals.MESSAGE, MessageCode.VALID_CREDENTIAL);
            return mapResult;
        }else {
            mapResult.put(Literals.MESSAGE, MessageCode.INVALID_CREDENTIAL);
            return mapResult;
        }
    }

    @Override
    public Map<String, Object> forgetPassword(String email) {
        Map<String, Object> mapResult = new HashMap<>();
        mapResult.put(Literals.STATUS, Literals.FALSE);
        User user = userRepository.findByEmail(email);
        if(Objects.isNull(user)){
            mapResult.put(Literals.MESSAGE, MessageCode.USER_ACCOUNT_NOT_FOUND);
            return mapResult;
        }

        int attempt = user.getResetPasswordAttempts();
        if(attempt >= MAX_ATTEMPTS){
            user.set_blocked(true);
            user.setActive(false);
            userRepository.save(user);
            emailService.sendEmailOnAccountBlock(user);
            mapResult.put(Literals.MESSAGE, MessageCode.ACCOUNT_BLOCKED_MESSAGE);
            return mapResult;
        }

        String resetToken = jwtTokenProvider.generatePasswordResetToken(user);
        ResetPasswordToken resetPasswordToken = new ResetPasswordToken(resetToken, user);
        resetPasswordTokenRepository.save(resetPasswordToken);
        String resetUrl = UrlMapping.APPLICATION_URL + UrlMapping.RESET_PASSWORD + UrlMapping.TOKEN + resetToken;

        emailService.sendEmailToUserOnForgetPassword(user, resetUrl);

        user.setResetPasswordAttempts(attempt + 1);
        userRepository.save(user);
        mapResult.put(Literals.RESPONSE, Literals.SUCCESS);
        mapResult.put(Literals.STATUS, Literals.TRUE);
        mapResult.put(Literals.MESSAGE, MessageCode.PASSWORD_RESET_LINK_SENT);
        return mapResult;
    }

    @Override
    public Map<String, Object> resetPassword(String token, String password, String confirmPassword) {
        Map<String, Object> mapResult = new HashMap<>();
        mapResult.put(Literals.STATUS, Literals.FALSE);

        if (!password.equals(confirmPassword)) {
            mapResult.put(Literals.MESSAGE, MessageCode.PASSWORD_MISMATCH_MESSAGE);
            return mapResult;
        }

        Optional<ResetPasswordToken> resetPasswordToken = resetPasswordTokenRepository.getResetPasswordTokenDetailsByToken(token);
        if(!resetPasswordToken.isPresent() || !jwtTokenProvider.validateToken(token)){
            mapResult.put(Literals.MESSAGE, MessageCode.INVALID_TOKEN_MESSAGE);
            return mapResult;
        }

        if(resetPasswordToken.get().isExpired()){
            mapResult.put(Literals.MESSAGE, MessageCode.INVALID_TOKEN_MESSAGE);
            return mapResult;
        }

        String userEmail = jwtTokenProvider.getEmailFromToken(token);
        User user = userRepository.findByEmail(userEmail);
        if(Objects.isNull(user)){
            mapResult.put(Literals.MESSAGE, MessageCode.USER_ACCOUNT_NOT_FOUND);
            return mapResult;
        }

        user.setPassword(passwordEncoder.encode(password));
        user.setResetPasswordAttempts(0);
        userRepository.save(user);
        resetPasswordTokenRepository.delete(resetPasswordToken.get());

        mapResult.put(Literals.RESPONSE, Literals.SUCCESS);
        mapResult.put(Literals.STATUS, Literals.TRUE);
        mapResult.put(Literals.MESSAGE, MessageCode.PASSWORD_RESET_SUCCESS_MESSAGE);
        return mapResult;
    }
}
