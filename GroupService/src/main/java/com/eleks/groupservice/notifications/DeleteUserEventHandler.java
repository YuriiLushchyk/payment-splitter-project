package com.eleks.groupservice.notifications;

import com.eleks.common.dto.DeleteUserEventDto;
import com.eleks.groupservice.service.GroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.integration.annotation.MessageEndpoint;

@MessageEndpoint
@Slf4j
class DeleteUserEventHandler {

    private final GroupService groupService;

    @Autowired
    public DeleteUserEventHandler(GroupService groupService) {
        this.groupService = groupService;
    }

    @StreamListener(Sink.INPUT)
    void handleDeleteUserEvent(DeleteUserEventDto event) {
        log.info("handleDeleteUserEvent, event = " + event.toString());
        groupService.deleteMemberFromAllGroups(event.getUserId());
    }
}