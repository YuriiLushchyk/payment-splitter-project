package com.eleks.groupservice.service;

import com.eleks.groupservice.dto.group.GroupRequestDto;
import com.eleks.groupservice.dto.group.GroupResponseDto;
import com.eleks.groupservice.exception.UsersIdsValidationException;
import com.eleks.groupservice.exception.ResourceNotFoundException;

import java.util.Optional;

public interface GroupService {
    GroupResponseDto saveGroup(GroupRequestDto group) throws UsersIdsValidationException;

    Optional<GroupResponseDto> getGroup(Long id);

    GroupResponseDto editGroup(Long id, GroupRequestDto requestDto) throws ResourceNotFoundException, UsersIdsValidationException;

    void deleteGroupById(Long id) throws ResourceNotFoundException;
}
