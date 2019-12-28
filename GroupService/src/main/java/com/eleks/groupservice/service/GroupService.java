package com.eleks.groupservice.service;

import com.eleks.groupservice.dto.GroupRequestDto;
import com.eleks.groupservice.dto.GroupResponseDto;

import java.util.Optional;

public interface GroupService {
    GroupResponseDto saveGroup(GroupRequestDto group);

    Optional<GroupResponseDto> getGroup(Long id);
}
