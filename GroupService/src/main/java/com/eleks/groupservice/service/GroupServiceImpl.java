package com.eleks.groupservice.service;

import com.eleks.groupservice.client.UserClient;
import com.eleks.groupservice.domain.Group;
import com.eleks.groupservice.dto.UserStatusDto;
import com.eleks.groupservice.dto.group.GroupRequestDto;
import com.eleks.groupservice.dto.group.GroupResponseDto;
import com.eleks.groupservice.exception.ResourceNotFoundException;
import com.eleks.groupservice.exception.UsersIdsValidationException;
import com.eleks.groupservice.mapper.GroupMapper;
import com.eleks.groupservice.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GroupServiceImpl implements GroupService {

    private GroupRepository repository;
    private UserClient client;

    @Autowired
    public GroupServiceImpl(GroupRepository repository, UserClient client) {
        this.repository = repository;
        this.client = client;
    }

    @Override
    public GroupResponseDto saveGroup(GroupRequestDto group) throws UsersIdsValidationException {
        if (!client.areUserIdsValid(group.getMembers())) {
            throw new UsersIdsValidationException("Group contains non existing users");
        }
        Group entity = GroupMapper.toEntity(group);
        Group savedEntity = repository.save(entity);
        return GroupMapper.toDto(savedEntity);
    }

    @Override
    public Optional<GroupResponseDto> getGroup(Long id) {
        return repository.findById(id).map(GroupMapper::toDto);
    }

    @Override
    public GroupResponseDto editGroup(Long id, GroupRequestDto requestDto) throws ResourceNotFoundException, UsersIdsValidationException {
        if (!repository.findById(id).isPresent()) {
            throw new ResourceNotFoundException("Group does't exist");
        }
        if (!client.areUserIdsValid(requestDto.getMembers())) {
            throw new UsersIdsValidationException("Group contains non existing users");
        }

        Group group = GroupMapper.toEntity(requestDto);
        group.setId(id);
        return GroupMapper.toDto(repository.save(group));
    }

    @Override
    public void deleteGroupById(Long id) throws ResourceNotFoundException {
        if (repository.findById(id).isPresent()) {
            repository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Group does't exist");
        }
    }

    @Override
    public List<UserStatusDto> getStatus(Long groupId, Long requesterId) throws ResourceNotFoundException, UsersIdsValidationException {
        return null;
    }
}
