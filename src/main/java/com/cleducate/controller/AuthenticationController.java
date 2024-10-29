package com.cleducate.controller;

import com.cleducate.constants.Literals;
import com.cleducate.constants.UrlMapping;
import com.cleducate.dto.LoginRequestDto;
import com.cleducate.service.AuthenticationService;
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
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService){
        this.authenticationService = authenticationService;
    }

    @Operation(summary = "User login", description = "Authenticates user and generates an API token for access.")
    @PostMapping(value = UrlMapping.LOGIN)
    public ResponseEntity<Object> userAuthentication(@Valid @RequestBody LoginRequestDto loginRequestDto){
        try {
            Map<String, Object> resultMap = authenticationService.authenticateUserAccount(loginRequestDto);
            if(resultMap.get(Literals.STATUS).equals(Literals.TRUE)){
                return ResponseHandler.response(resultMap.get(Literals.RESPONSE), resultMap.get(Literals.MESSAGE).toString(),true, HttpStatus.OK);
            }
            return ResponseHandler.response(null, resultMap.get(Literals.MESSAGE).toString(), false, HttpStatus.BAD_REQUEST);
        }catch (Exception e){
           log.error(Literals.CATCH_EXCEPTION, e.getMessage());
           return ResponseHandler.response(null, Literals.SOMETHING_WENT_WRONG, false, HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Forget Password", description = "Initiates the password recovery process. A password reset link will be sent to the user's registered email address.")
    @PostMapping(value = UrlMapping.FORGET_PASSWORD)
    public ResponseEntity<Object> forgetPassword(@RequestParam(required = true) String email){
        try {
            Map<String, Object> resultMap = authenticationService.forgetPassword(email);
            if(resultMap.get(Literals.STATUS).equals(Literals.TRUE)){
                return ResponseHandler.response(resultMap.get(Literals.RESPONSE), resultMap.get(Literals.MESSAGE).toString(),true, HttpStatus.OK);
            }
            return ResponseHandler.response(null, resultMap.get(Literals.MESSAGE).toString(), false, HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            log.error(Literals.CATCH_EXCEPTION, e.getMessage());
            return ResponseHandler.response(null, Literals.SOMETHING_WENT_WRONG, false, HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Reset Password", description = "Initiates the password reset process for a user. A reset link will be sent to the registered email address.")
    @PutMapping(value = UrlMapping.RESET_PASSWORD)
    public ResponseEntity<Object> resetPassword(@RequestParam(required = true) String token,
                                                @RequestParam(required = true) String password,
                                                @RequestParam(required = true) String confirmPassword){
        try {
            Map<String, Object> resultMap = authenticationService.resetPassword(token, password, confirmPassword);
            if(resultMap.get(Literals.STATUS).equals(Literals.TRUE)){
                return ResponseHandler.response(resultMap.get(Literals.RESPONSE), resultMap.get(Literals.MESSAGE).toString(),true, HttpStatus.OK);
            }
            return ResponseHandler.response(null, resultMap.get(Literals.MESSAGE).toString(), false, HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            log.error(Literals.CATCH_EXCEPTION, e.getMessage());
            return ResponseHandler.response(null, Literals.SOMETHING_WENT_WRONG, false, HttpStatus.BAD_REQUEST);
        }
    }




}
