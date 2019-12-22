package com.eleks.userservice.repository;

import com.eleks.userservice.domain.User;

import java.util.List;

public interface UserRepository {
    User findById(Long id);

    List<User> findAll();

    User saveUser(User user);

    User findByUsername(String username);

    User findByEmail(String email);

    void deleteById(Long id);
}
