package com.cleducate.service;

import com.cleducate.dto.LoginRequestDto;
import com.cleducate.entity.Session;
import com.cleducate.entity.User;
import com.cleducate.repository.SessionRepository;
import com.cleducate.repository.UserRepository;
import com.cleducate.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
@Service
public class SessionServiceImpl implements SessionService {

    private final SessionRepository sessionRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    @Autowired
    public SessionServiceImpl(SessionRepository sessionRepository, JwtTokenProvider jwtTokenProvider, UserRepository userRepository){
        this.sessionRepository = sessionRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
    }

    @Override
    public Session createSession(User user, LoginRequestDto credentials) {
        Session session = new Session();
        Date loginTime = new Date();
        session.setLoginTime(loginTime);
        session.setPlatform(credentials.getPlatform());
        session.setLastLoginTime(loginTime);
        session.setUser(user);
        userRepository.save(user);
        sessionRepository.save(session);
        session.setAuthToken(jwtTokenProvider.generateAuthToken(user));
        return session;
    }


}
