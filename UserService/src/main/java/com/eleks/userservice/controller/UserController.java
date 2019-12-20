package com.eleks.userservice.controller;

import com.eleks.userservice.dto.user.UserRequestDto;
import com.eleks.userservice.dto.user.UserResponseDto;
import com.eleks.userservice.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
public class UserController {

    private final UserService service;

    @GetMapping("/users/{id}")
    public UserResponseDto getUser(@PathVariable Long id) {
        return service.getUser(id);
    }

    @GetMapping("/users")
    public List<UserResponseDto> getUsers() {
        return service.getUsers();
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto saveUser(@Valid @RequestBody UserRequestDto user) {
        return service.saveUser(user);
    }

    @PutMapping("/users/{id}")
    public UserResponseDto editUser(@PathVariable Long id, @Valid @RequestBody UserRequestDto user) {

        return service.editUser(id, user);
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable Long id) {
        service.deleteUserById(id);
    }
}
