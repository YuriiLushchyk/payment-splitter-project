package com.eleks.userservice.service;

import com.eleks.userservice.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto getUser(Long id);

    List<UserDto> getUsers();

    UserDto saveUser(UserDto user);

    UserDto editUser(Long id, UserDto user);

    void deleteUserById(Long id);
}
