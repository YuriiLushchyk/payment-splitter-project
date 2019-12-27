package com.eleks.groupservice.client;

import com.eleks.groupservice.dto.UserSearchDto;
import com.eleks.groupservice.exception.UserServiceException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserClientTest {

    static WireMockServer wm = new WireMockServer(7777);

    static List<Long> userIds;

    @Autowired
    UserClient client;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeAll
    static void setUp() {
        wm.start();
        userIds = Arrays.asList(1L, 2L, 3L);
    }

    @AfterAll
    static void cleanUp() {
        wm.stop();
    }

    @Test
    void areUserIdsValid_ServiceReturnsSameCountOfIdsAsRequested_ShouldReturnTrue() throws Exception {
        UserSearchDto searchDto = new UserSearchDto(userIds);

        wm.stubFor(post(urlEqualTo("/users/search"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBodyFile("user_search_response_with_three_users.json")));

        boolean isValid = client.areUserIdsValid(userIds);

        verifyPostOnSearchWithRequestDto(searchDto);
        assertTrue(isValid);
    }

    @Test
    void areUserIdsValid_ServiceReturnsLessCountOfIdsAsRequested_ShouldReturnFalse() throws Exception {
        UserSearchDto searchDto = new UserSearchDto(userIds);

        wm.stubFor(post(urlEqualTo("/users/search"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBodyFile("user_search_response_with_two_users.json")));

        boolean isValid = client.areUserIdsValid(userIds);

        verifyPostOnSearchWithRequestDto(searchDto);
        assertFalse(isValid);
    }

    @Test
    void areUserIdsValid_ServiceReturnsBadRequest_ShouldReturnFalse() throws Exception {
        UserSearchDto searchDto = new UserSearchDto(userIds);

        wm.stubFor(post(urlEqualTo("/users/search"))
                .willReturn(status(400)));

        boolean isValid = client.areUserIdsValid(userIds);

        verifyPostOnSearchWithRequestDto(searchDto);
        assertFalse(isValid);
    }

    @Test
    void areUserIdsValid_ServiceReturnsServerError_ShouldThrowException() throws Exception {
        UserSearchDto searchDto = new UserSearchDto(userIds);

        wm.stubFor(post(urlEqualTo("/users/search"))
                .willReturn(status(500)));

        UserServiceException exception = assertThrows(UserServiceException.class,
                () -> client.areUserIdsValid(userIds));

        verifyPostOnSearchWithRequestDto(searchDto);
        assertEquals("Server error during request to UserService", exception.getMessage());
    }

    private void verifyPostOnSearchWithRequestDto(UserSearchDto dto) throws Exception {
        wm.verify((postRequestedFor(urlEqualTo("/users/search"))
                .withHeader("Content-Type", equalTo(MediaType.APPLICATION_JSON_VALUE))
                .withRequestBody(equalToJson(objectMapper.writeValueAsString(dto)))));
    }
}