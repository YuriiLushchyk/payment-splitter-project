package com.eleks.groupservice.service;

import com.eleks.groupservice.client.UserClient;
import com.eleks.groupservice.domain.Group;
import com.eleks.groupservice.dto.GroupRequestDto;
import com.eleks.groupservice.dto.GroupResponseDto;
import com.eleks.groupservice.exception.GroupMembersIdsValidationException;
import com.eleks.groupservice.mapper.GroupMapper;
import com.eleks.groupservice.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public GroupResponseDto saveGroup(GroupRequestDto group) {
        if (!client.areUserIdsValid(group.getMembers())) {
            throw new GroupMembersIdsValidationException("Group contains non existing user's ids");
        }
        Group entity = GroupMapper.toEntity(group);
        Group savedEntity = repository.save(entity);
        return GroupMapper.toDto(savedEntity);
    }

    @Override
    public Optional<GroupResponseDto> getGroup(Long id) {
        return Optional.empty();
    }

    @Override
    public GroupResponseDto editGroup(Long id, GroupRequestDto requestDto) {
        return null;
    }
}
