package com.eleks.userservice.controller;


import com.eleks.userservice.domain.User;
import com.eleks.userservice.dto.login.JwtResponse;
import com.eleks.userservice.dto.login.LoginRequest;
import com.eleks.userservice.security.JwtTokenUtil;
import com.eleks.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private JwtTokenUtil jwtTokenUtil;
    private UserService service;

    @Autowired
    public AuthController(JwtTokenUtil jwtTokenUtil, UserService service) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.service = service;
    }

    @PostMapping(value = "/login")
    public JwtResponse createAuthenticationToken(@RequestBody LoginRequest request) {
        User user = service.getUserByUsernameAndPassword(request.getUsername(), request.getPassword());
        return new JwtResponse(jwtTokenUtil.generateToken(user.getId()));
    }
}
