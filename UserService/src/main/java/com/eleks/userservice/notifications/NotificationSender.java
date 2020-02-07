package com.eleks.userservice.notifications;

import com.eleks.common.dto.DeleteUserEventDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
public class NotificationSender {

    private final MessageChannel deleteUserMsgChannel;

    public NotificationSender(@Qualifier(Source.OUTPUT) MessageChannel deleteUserMsgChannel) {
        this.deleteUserMsgChannel = deleteUserMsgChannel;
    }

    public void sendDeleteUserEvent(Long userId) {
        deleteUserMsgChannel.send(MessageBuilder.withPayload(new DeleteUserEventDto(userId)).build());
    }
}
