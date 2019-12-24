package com.eleks.groupservice.service;

import com.eleks.groupservice.dto.GroupRequestDto;
import com.eleks.groupservice.dto.GroupResponseDto;

public interface GroupService {
    GroupResponseDto saveGroup(GroupRequestDto group);
}
