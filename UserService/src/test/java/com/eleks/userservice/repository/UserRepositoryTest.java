package com.eleks.userservice.repository;

import com.eleks.userservice.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    UserRepository repository;

    User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .username("username")
                .firstName("firstName")
                .lastName("lastName")
                .dateOfBirth(LocalDate.now())
                .email("email@eleks.com")
                .receiveNotifications(false)
                .build();
    }

    @Test
    void findByUsername_UserWithSuchUsernameExists_ReturnUser() {
        User saved = repository.save(user);

        User found = repository.findByUsername(user.getUsername()).get();

        assertEquals(saved.getId(), found.getId());

        repository.deleteById(saved.getId());
    }

    @Test
    void findByUsername_UserWithSuchUsernameDoesntExist_ReturnNull() {

        Optional<User> found = repository.findByUsername(user.getUsername());

        assertFalse(found.isPresent());
    }

    @Test
    void findByEmail_UserWithSuchEmailExists_ReturnUser() {
        User saved = repository.save(user);

        User found = repository.findByEmail(user.getEmail()).get();

        assertEquals(saved.getId(), found.getId());

        repository.deleteById(saved.getId());
    }

    @Test
    void findByEmail_UserWithSuchEmailDoesntExist_ReturnNull() {
        Optional<User> found = repository.findByEmail(user.getEmail());
        assertFalse(found.isPresent());
    }
}