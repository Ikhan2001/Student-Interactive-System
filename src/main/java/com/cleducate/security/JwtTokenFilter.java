package com.cleducate.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public JwtTokenFilter(JwtTokenProvider jwtTokenProvider){
        this.jwtTokenProvider = jwtTokenProvider;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            final String requestTokenHeader = request.getHeader("Authorization");
            String jwtToken = null;

            if(requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
                jwtToken = requestTokenHeader.substring(7);
            }else {
                logger.warn("Jwt token does not begin with Bearer String");
            }

            if(jwtToken != null && jwtTokenProvider.validateToken(jwtToken)){
                Authentication auth = jwtTokenProvider.getAuthentication(jwtToken);
                SecurityContextHolder.getContext().setAuthentication(auth);
                populateRequestWithHeaders(request, jwtToken);
            }

        }catch (Exception e){
            SecurityContextHolder.clearContext();
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
            return;
        }
        filterChain.doFilter(request, response);

    }

    private void populateRequestWithHeaders(HttpServletRequest request, String jwtToken){
        Jws<Claims> claims = jwtTokenProvider.resolveClaims(jwtToken);
        request.setAttribute("id", claims.getBody().get("userId"));
        request.setAttribute("email", claims.getBody().getSubject());
    }
}
