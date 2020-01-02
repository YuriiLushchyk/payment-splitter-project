package com.eleks.groupservice.controller;

import com.eleks.groupservice.dto.UserStatusDto;
import com.eleks.groupservice.dto.group.GroupRequestDto;
import com.eleks.groupservice.dto.group.GroupResponseDto;
import com.eleks.groupservice.exception.ResourceNotFoundException;
import com.eleks.groupservice.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class GroupController {

    private GroupService service;

    @Autowired
    public GroupController(GroupService service) {
        this.service = service;
    }

    @PostMapping("/groups")
    public GroupResponseDto saveGroup(@Valid @RequestBody GroupRequestDto group) {
        return service.saveGroup(group);
    }

    @GetMapping("/groups/{id}")
    public GroupResponseDto getGroup(@PathVariable Long id) {
        return service.getGroup(id).orElseThrow(() -> new ResourceNotFoundException("group with this id does't exist"));
    }

    @PutMapping("/groups/{id}")
    public GroupResponseDto editGroup(@PathVariable Long id, @Valid @RequestBody GroupRequestDto group) {
        return service.editGroup(id, group);
    }

    @DeleteMapping("/groups/{id}")
    public void deleteGroup(@PathVariable Long id) {
        service.deleteGroupById(id);
    }

    @GetMapping("/groups/{groupId}/users/{userId}/status")
    public List<UserStatusDto> getGroupMembersStatus(@PathVariable Long groupId, @PathVariable Long userId) {
        return service.getGroupMembersStatus(groupId, userId);
    }
}
