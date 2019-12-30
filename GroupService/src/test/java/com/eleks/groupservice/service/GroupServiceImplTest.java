package com.eleks.groupservice.service;

import com.eleks.groupservice.client.UserClient;
import com.eleks.groupservice.domain.Currency;
import com.eleks.groupservice.domain.Group;
import com.eleks.groupservice.dto.group.GroupRequestDto;
import com.eleks.groupservice.dto.group.GroupResponseDto;
import com.eleks.groupservice.exception.ResourceNotFoundException;
import com.eleks.groupservice.exception.UsersIdsValidationException;
import com.eleks.groupservice.repository.GroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
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
        service = new GroupServiceImpl(repository, client);

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
    void saveGroup_GroupMembersAreNotExist_ShouldThrowUsersIdsValidationException() {
        when(client.areUserIdsValid(requestDto.getMembers())).thenReturn(false);

        UsersIdsValidationException exception = assertThrows(UsersIdsValidationException.class,
                () -> service.saveGroup(requestDto));

        assertEquals("Group contains non existing users", exception.getMessage());
    }

    @Test
    void getGroup_GroupExists_ReturnGroupResponseDto() {
        when(repository.findById(entity.getId())).thenReturn(Optional.of(entity));

        Optional<GroupResponseDto> result = service.getGroup(entity.getId());

        assertTrue(result.isPresent());
    }

    @Test
    void getGroup_GroupDoesntExist_ReturnEmptyOptional() {
        when(repository.findById(entity.getId())).thenReturn(Optional.empty());

        Optional<GroupResponseDto> result = service.getGroup(entity.getId());

        assertFalse(result.isPresent());
    }

    @Test
    void editGroup_GroupAndMembersAreExist_ReturnResponseDto() {
        when(repository.findById(entity.getId())).thenReturn(Optional.of(entity));
        when(client.areUserIdsValid(requestDto.getMembers())).thenReturn(true);

        when(repository.save(any(Group.class))).thenReturn(entity);

        GroupResponseDto result = service.editGroup(entity.getId(), requestDto);

        assertNotNull(result);
    }

    @Test
    void editGroup_GroupDoesntExist_ThrowResourceNotFoundException() {
        when(repository.findById(entity.getId())).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> service.editGroup(entity.getId(), requestDto));

        assertEquals("Group does't exist", ex.getMessage());
    }

    @Test
    void editGroup_GroupExistMembersAreNotValid_ThrowUsersIdsValidationException() {
        when(repository.findById(entity.getId())).thenReturn(Optional.of(entity));
        when(client.areUserIdsValid(requestDto.getMembers())).thenReturn(false);

        UsersIdsValidationException exception = assertThrows(UsersIdsValidationException.class,
                () -> service.saveGroup(requestDto));

        assertEquals("Group contains non existing users", exception.getMessage());
    }


    @Test
    public void deleteGroupById_GroupWithIdExists_CallRepositoryDelete() {
        Long id = 1L;
        when(repository.findById(id)).thenReturn(Optional.of(entity));

        service.deleteGroupById(id);

        verify(repository).deleteById(id);
    }

    @Test
    public void deleteGroupById_GroupWithIdDoesntExist_ThrowResourceNotFoundException() {
        Long id = 1L;
        when(repository.findById(id)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> service.deleteGroupById(id));

        assertEquals("Group does't exist", exception.getMessage());
    }
}