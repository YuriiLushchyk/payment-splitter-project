package com.eleks.groupservice.service;

import com.eleks.groupservice.client.UserClient;
import com.eleks.groupservice.domain.Currency;
import com.eleks.groupservice.domain.Group;
import com.eleks.groupservice.dto.GroupRequestDto;
import com.eleks.groupservice.dto.GroupResponseDto;
import com.eleks.groupservice.exception.GroupMembersIdsValidationException;
import com.eleks.groupservice.mapper.GroupMapper;
import com.eleks.groupservice.repository.GroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GroupServiceImplTest {

    @Mock
    GroupRepository repository;

    @Mock
    UserClient client;

    GroupServiceImpl service;

    GroupRequestDto requestDto;

    Group entity;

    @BeforeEach
    void setUp() {
        service = new GroupServiceImpl(repository, client, new GroupMapper());

        requestDto = GroupRequestDto.builder()
                .groupName("groupName")
                .currency(Currency.EUR)
                .members(Arrays.asList(1L, 2L, 3L))
                .build();

        entity = Group.builder()
                .id(21L)
                .groupName(requestDto.getGroupName())
                .currency(requestDto.getCurrency())
                .members(requestDto.getMembers())
                .build();
    }

    @Test
    void saveGroup_GroupMembersAreExist_ReturnSavedUser() {
        when(client.areUserIdsValid(requestDto.getMembers())).thenReturn(true);
        when(repository.save(any(Group.class))).thenReturn(entity);

        GroupResponseDto responseDto = service.saveGroup(requestDto);

        assertNotNull(responseDto);
    }

    @Test
    void saveGroup_GroupMembersAreNotExist_ShouldThrowException() {
        when(client.areUserIdsValid(requestDto.getMembers())).thenReturn(false);

        GroupMembersIdsValidationException exception = assertThrows(GroupMembersIdsValidationException.class,
                () -> service.saveGroup(requestDto));

        assertEquals("Group contains non existing user's ids", exception.getMessage());
    }
}