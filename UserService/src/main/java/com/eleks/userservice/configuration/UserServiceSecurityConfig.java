package com.eleks.userservice.configuration;

import com.eleks.common.security.BaseSecurityConfig;
import com.eleks.common.security.JwtTokenUtil;
import com.eleks.userservice.service.CustomUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class UserServiceSecurityConfig extends BaseSecurityConfig {

    private CustomUserDetailsService userDetailsService;

    @Autowired
    public UserServiceSecurityConfig(ObjectMapper objectMapper, JwtTokenUtil jwtTokenUtil, CustomUserDetailsService userDetailsService) {
        super(objectMapper, jwtTokenUtil);
        this.userDetailsService = userDetailsService;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
