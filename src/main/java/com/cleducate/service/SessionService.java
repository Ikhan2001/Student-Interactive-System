package com.cleducate.service;

import com.cleducate.dto.LoginRequestDto;
import com.cleducate.entity.Session;
import com.cleducate.entity.User;

public interface SessionService {

    Session createSession(User user, LoginRequestDto loginRequestDto);

}
