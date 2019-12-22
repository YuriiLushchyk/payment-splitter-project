package com.eleks.userservice.service;

import com.eleks.userservice.domain.User;
import com.eleks.userservice.dto.user.UserRequestDto;
import com.eleks.userservice.dto.user.UserResponseDto;
import com.eleks.userservice.exception.ResourceNotFoundException;
import com.eleks.userservice.exception.UniqueUserPropertiesViolationException;
import com.eleks.userservice.mapper.UserMapper;
import com.eleks.userservice.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final UserMapper mapper;

    @Override
    public UserResponseDto getUser(Long id) {
        User user = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("user with this id does't exist"));
        return mapper.toDto(user);
    }

    @Override
    public List<UserResponseDto> getUsers() {
        return repository.findAll()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseDto saveUser(UserRequestDto user) {
        if (repository.findByUsername(user.getUsername()).isPresent()) {
            throw new UniqueUserPropertiesViolationException("user with this username already exists");
        } else if (repository.findByEmail(user.getEmail()).isPresent()) {
            throw new UniqueUserPropertiesViolationException("user with this email already exists");
        } else {
            User entity = mapper.toEntity(user);
            User savedEntity = repository.save(entity);
            return mapper.toDto(savedEntity);
        }
    }

    @Override
    public UserResponseDto editUser(Long id, UserRequestDto user) {
        if (repository.findById(id).isPresent()) {
            User entity = mapper.toEntity(user);
            entity.setId(id);
            User saved = repository.save(entity);
            return mapper.toDto(saved);
        } else {
            throw new ResourceNotFoundException("user with this id does't exist");
        }
    }

    @Override
    public void deleteUserById(Long id) {
        if (repository.findById(id).isPresent()) {
            repository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("user with this id does't exist");
        }
    }
}
