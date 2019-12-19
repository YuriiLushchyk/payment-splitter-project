package com.eleks.userservice.controller;

import com.eleks.userservice.dto.UserDto;
import com.eleks.userservice.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
public class UserController {

    private final UserService service;

    @GetMapping("/users/{id}")
    public UserDto getUser(@PathVariable Long id) {
        return service.getUser(id);
    }

    @GetMapping("/users")
    public List<UserDto> getUsers() {
        return service.getUsers();
    }

    @PostMapping("/users")
    public UserDto saveUser(@Valid @RequestBody UserDto user) {
        return service.saveUser(user);
    }
}
