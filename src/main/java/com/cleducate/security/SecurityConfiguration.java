package com.cleducate.security;

import com.cleducate.constants.UrlMapping;
import com.cleducate.enums.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtTokenFilter jwtTokenFilter;

    @Autowired
    public SecurityConfiguration(JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint, JwtTokenFilter jwtTokenFilter){
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtTokenFilter = jwtTokenFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.headers().defaultsDisabled().disable();
        http.csrf().disable();
        http.authorizeHttpRequests(
                (request)->
                        request.antMatchers(UrlMapping.LOGIN, UrlMapping.COMPLETE_PROFILE, UrlMapping.FORGET_PASSWORD, UrlMapping.RESET_PASSWORD).permitAll()
                                .antMatchers(UrlMapping.API_V1 + UrlMapping.ADMIN + "/**" , UrlMapping.REGISTER_CLIENT).hasAuthority(Roles.SUPER_ADMIN.name())
                                .antMatchers(UrlMapping.API_V1 + UrlMapping.CLIENT + "/**").hasAuthority(Roles.CLIENT.name())
                                .anyRequest().authenticated()
        ).exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint).and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .cors();

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return web -> web.ignoring().antMatchers("/swagger-resources/**")
                .antMatchers("/swagger-ui/**")
                .antMatchers("/v3/api-docs/**")
                .antMatchers("/configuration/**")
                .antMatchers("/v3/api-docs/swagger-config")
                .antMatchers("/webjars/**")
                .antMatchers("/public")
                .and()
                .ignoring()
                .antMatchers("/h2-console/**/**");
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception{
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }



}
