package com.eleks.groupservice.mapper;

import com.eleks.groupservice.domain.Group;
import com.eleks.groupservice.dto.GroupRequestDto;
import com.eleks.groupservice.dto.GroupResponseDto;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class GroupMapper {

    public Group toEntity(GroupRequestDto dto) {
        if (Objects.isNull(dto)) {
            return null;
        } else {
            return Group.builder()
                    .groupName(dto.getGroupName())
                    .currency(dto.getCurrency())
                    .members(dto.getMembers())
                    .build();
        }
    }

    public GroupResponseDto toDto(Group entity) {
        if (Objects.isNull(entity)) {
            return null;
        } else {
            return GroupResponseDto.builder()
                    .id(entity.getId())
                    .groupName(entity.getGroupName())
                    .currency(entity.getCurrency())
                    .members(entity.getMembers())
                    .build();
        }
    }
}
