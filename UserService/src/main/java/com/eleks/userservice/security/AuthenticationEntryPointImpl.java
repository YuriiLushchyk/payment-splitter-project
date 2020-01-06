package com.eleks.userservice.security;


import com.eleks.userservice.dto.ErrorDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;

@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

    private ObjectMapper objectMapper;

    @Autowired
    public AuthenticationEntryPointImpl(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        String msg;
        if (authException instanceof BadCredentialsException) {
            msg = authException.getMessage();
        } else {
            msg = "Unauthorized";
        }

        ErrorDto errorDto = ErrorDto.builder()
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .messages(Collections.singletonList(msg))
                .timestamp(LocalDateTime.now())
                .build();
        objectMapper.writeValue(response.getOutputStream(), errorDto);
    }
}