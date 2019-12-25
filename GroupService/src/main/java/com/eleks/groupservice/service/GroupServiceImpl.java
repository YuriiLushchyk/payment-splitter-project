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

@Service
public class GroupServiceImpl implements GroupService {

    private GroupRepository repository;
    private UserClient client;
    private GroupMapper mapper;

    @Autowired
    public GroupServiceImpl(GroupRepository repository, UserClient client, GroupMapper mapper) {
        this.repository = repository;
        this.client = client;
        this.mapper = mapper;
    }

    @Override
    public GroupResponseDto saveGroup(GroupRequestDto group) {
        if (!client.areUserIdsValid(group.getMembers())) {
            throw new GroupMembersIdsValidationException("Group contains non existing user's ids");
        }
        Group entity = mapper.toEntity(group);
        Group savedEntity = repository.save(entity);
        return mapper.toDto(savedEntity);
    }
}
