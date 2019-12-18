package com.eleks.userservice.controller;

import com.eleks.userservice.dto.UserDto;
import com.eleks.userservice.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class UserController {

    private final UserService service;

    @GetMapping("/users/{id}")
    public UserDto getUser(@PathVariable Long id) {
        return service.getUser(id);
    }

}
