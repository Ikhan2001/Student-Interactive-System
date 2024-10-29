package com.cleducate.utils;

import com.cleducate.entity.User;
import com.cleducate.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class GenericUtils {

    private final UserRepository userRepository;
    private final HttpServletRequest request;

    @Autowired
    public GenericUtils(UserRepository userRepository, HttpServletRequest request){
        this.userRepository = userRepository;
        this.request = request;
    }

    public User getLoggedInUser(){
        long userId = Long.parseLong(request.getAttribute("id").toString());
        return userRepository.findById(userId).orElse(null);
    }









}
