package com.eleks.userservice.controller;


import com.eleks.common.security.JwtTokenUtil;
import com.eleks.common.security.model.JwtUserDataClaim;
import com.eleks.userservice.dto.CustomUserDetails;
import com.eleks.userservice.dto.login.JwtResponse;
import com.eleks.userservice.dto.login.LoginRequest;
import com.eleks.userservice.service.CustomUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@Slf4j
public class AuthController {

    private JwtTokenUtil jwtTokenUtil;
    private CustomUserDetailsService service;
    private AuthenticationManager authenticationManager;

    @Autowired
    public AuthController(JwtTokenUtil jwtTokenUtil, CustomUserDetailsService service, AuthenticationManager authenticationManager) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.service = service;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping(value = "/login")
    public JwtResponse login(@Valid @RequestBody LoginRequest request) throws IOException, BadCredentialsException {
        authenticate(request.getUsername(), request.getPassword());

        CustomUserDetails details = service.loadUserByUsername(request.getUsername());
        JwtUserDataClaim userDataClaim = new JwtUserDataClaim(details.getUsername(), details.getUserId());

        return new JwtResponse(jwtTokenUtil.generateToken(userDataClaim));
    }

    private void authenticate(String username, String password) throws BadCredentialsException {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid credentials");
        }
    }
}
