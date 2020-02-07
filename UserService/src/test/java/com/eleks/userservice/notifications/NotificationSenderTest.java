package com.eleks.userservice.notifications;

import com.eleks.common.dto.DeleteUserEventDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.cloud.stream.test.binder.MessageCollector;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class NotificationSenderTest {

    @Autowired
    private Source source;

    @Autowired
    private NotificationSender sender;

    @Autowired
    private MessageCollector messageCollector;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void sendDeleteUserEvent() throws Exception {
        Long userId = 1L;
        sender.sendDeleteUserEvent(userId);

        String rawPayload = (String) messageCollector.forChannel(source.output()).poll().getPayload();
        DeleteUserEventDto event = objectMapper.readValue(rawPayload, DeleteUserEventDto.class);

        assertEquals(userId, event.getUserId());
    }
}