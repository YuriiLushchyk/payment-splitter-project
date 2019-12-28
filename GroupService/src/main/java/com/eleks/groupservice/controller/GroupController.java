package com.eleks.groupservice.controller;

import com.eleks.groupservice.dto.GroupRequestDto;
import com.eleks.groupservice.dto.GroupResponseDto;
import com.eleks.groupservice.exception.ResourceNotFoundException;
import com.eleks.groupservice.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
}
