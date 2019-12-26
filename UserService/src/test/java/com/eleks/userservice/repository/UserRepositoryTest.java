package com.eleks.userservice.repository;

import com.eleks.userservice.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    void findAllByIdIn_SaveTwoUsersAndFindThemByIds_ReturnTwoUsers() {
        List<User> users = Arrays.asList(
                User.builder()
                        .username("username1")
                        .firstName("firstName1")
                        .lastName("lastName1")
                        .dateOfBirth(LocalDate.now())
                        .email("email1@eleks.com")
                        .receiveNotifications(false)
                        .build(),
                User.builder()
                        .username("username2")
                        .firstName("firstName2")
                        .lastName("lastName2")
                        .dateOfBirth(LocalDate.now())
                        .email("email2@eleks.com")
                        .receiveNotifications(false)
                        .build());
        List<User> savedUsers = repository.saveAll(users);
        List<Long> ids = savedUsers.stream().map(User::getId).collect(Collectors.toList());

        Optional<List<User>> found = repository.findAllByIdIn(ids);

        assertTrue(found.isPresent());
        assertEquals(users.size(), found.get().size());
    }

    @Test
    void findAllByIdIn_NoUsersAndFindByIds_ReturnTwoUsers() {
        Optional<List<User>> found = repository.findAllByIdIn(Arrays.asList(1L, 2L));

        assertFalse(found.isPresent());
    }
}