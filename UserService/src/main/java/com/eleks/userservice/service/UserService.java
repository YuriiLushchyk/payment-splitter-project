package com.eleks.userservice.service;

import com.eleks.userservice.dto.UserDto;

public interface UserService {

    UserDto getUser(Long id);
}
