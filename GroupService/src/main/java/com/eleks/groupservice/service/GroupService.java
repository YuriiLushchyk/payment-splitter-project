package com.eleks.groupservice.service;

import com.eleks.groupservice.dto.UserStatusDto;
import com.eleks.groupservice.dto.group.GroupRequestDto;
import com.eleks.groupservice.dto.group.GroupResponseDto;
import com.eleks.groupservice.exception.ResourceNotFoundException;
import com.eleks.groupservice.exception.UsersIdsValidationException;

import java.util.List;
import java.util.Optional;

public interface GroupService {
    GroupResponseDto saveGroup(GroupRequestDto group) throws UsersIdsValidationException;

    Optional<GroupResponseDto> getGroup(Long id);

    GroupResponseDto editGroup(Long id, GroupRequestDto requestDto) throws ResourceNotFoundException, UsersIdsValidationException;

    void deleteGroupById(Long id) throws ResourceNotFoundException;

    List<UserStatusDto> getGroupMembersStatus(Long groupId, Long requesterId) throws ResourceNotFoundException, UsersIdsValidationException;
}
