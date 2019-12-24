package com.eleks.groupservice.controller;

import com.eleks.groupservice.dto.GroupRequestDto;
import com.eleks.groupservice.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class GroupController {

    private GroupService service;

    @Autowired
    public GroupController(GroupService service) {
        this.service = service;
    }

    @PostMapping("/groups")
    public GroupRequestDto saveGroup(@Valid @RequestBody GroupRequestDto group) {
        return service.saveGroup(group);
    }
}
