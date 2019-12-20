package com.eleks.userservice.service;

import com.eleks.userservice.dto.user.UserRequestDto;
import com.eleks.userservice.dto.user.UserResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Override
    public UserResponseDto getUser(Long id) {
        return null;
    }

    @Override
    public List<UserResponseDto> getUsers() {
        return null;
    }

    @Override
    public UserResponseDto saveUser(UserRequestDto user) {
        return null;
    }

    @Override
    public UserResponseDto editUser(Long id, UserRequestDto user) {
        return null;
    }

    @Override
    public void deleteUserById(Long id) {

    }
}
