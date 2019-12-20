package com.eleks.userservice.service;

import com.eleks.userservice.dto.user.UserRequestDto;
import com.eleks.userservice.dto.user.UserResponseDto;

import java.util.List;

public interface UserService {

    UserResponseDto getUser(Long id);

    List<UserResponseDto> getUsers();

    UserResponseDto saveUser(UserRequestDto user);

    UserResponseDto editUser(Long id, UserRequestDto user);

    void deleteUserById(Long id);
}
