package com.eleks.userservice.service;

import com.eleks.userservice.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto getUser(Long id);

    List<UserDto> getUsers();
}
