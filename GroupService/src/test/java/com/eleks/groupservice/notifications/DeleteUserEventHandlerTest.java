package com.eleks.groupservice.notifications;

import com.eleks.common.dto.DeleteUserEventDto;
import com.eleks.groupservice.service.GroupService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.support.MessageBuilder;

import static org.mockito.Mockito.verify;

@SpringBootTest
class DeleteUserEventHandlerTest {

    @Autowired
    private Sink sink;

    @SpyBean
    private DeleteUserEventHandler handlerSpy;

    @MockBean
    private GroupService serviceMock;

    @Test
    void handleDeleteUserEvent_SendEventAboutUserDelete_DeleteShouldBeInvoked() {
        Long userId = 1L;
        DeleteUserEventDto event = new DeleteUserEventDto(userId);
        sink.input().send(MessageBuilder.withPayload(event).build());

        verify(handlerSpy).handleDeleteUserEvent(event);
        verify(serviceMock).deleteMemberFromAllGroups(userId);
    }
}