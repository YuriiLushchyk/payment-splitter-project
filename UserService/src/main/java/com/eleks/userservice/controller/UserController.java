package com.eleks.userservice.controller;

import com.eleks.userservice.dto.UserDto;
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
    public UserDto getUser(@PathVariable Long id) {
        return service.getUser(id);
    }

    @GetMapping("/users")
    public List<UserDto> getUsers() {
        return service.getUsers();
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto saveUser(@Valid @RequestBody UserDto user) {
        return service.saveUser(user);
    }

    @PutMapping("/users/{id}")
    public UserDto editUser(@PathVariable Long id, @Valid @RequestBody UserDto user) {
        return service.editUser(id, user);
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable Long id) {
        service.deleteUserById(id);
    }
}
