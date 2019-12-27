package com.eleks.groupservice.controller;

import com.eleks.groupservice.advisor.ResponseExceptionHandler;
import com.eleks.groupservice.domain.Currency;
import com.eleks.groupservice.dto.ErrorDto;
import com.eleks.groupservice.dto.GroupRequestDto;
import com.eleks.groupservice.dto.GroupResponseDto;
import com.eleks.groupservice.exception.GroupMembersIdsValidationException;
import com.eleks.groupservice.exception.UserServiceException;
import com.eleks.groupservice.service.GroupService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@WebMvcTest
class GroupControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private GroupController controller;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private GroupService groupService;

    private GroupRequestDto requestDto;
    private GroupResponseDto responseDto;

    @BeforeEach
    void setUp() {
        mockMvc = standaloneSetup(controller)
                .setControllerAdvice(new ResponseExceptionHandler())
                .build();

        requestDto = GroupRequestDto.builder()
                .groupName("groupName")
                .currency(Currency.EUR)
                .members(Arrays.asList(1L, 2L, 3L))
                .build();

        responseDto = GroupResponseDto.builder()
                .id(21L)
                .groupName(requestDto.getGroupName())
                .currency(requestDto.getCurrency())
                .members(requestDto.getMembers())
                .build();
    }

    @Test
    void saveGroup_AllDataIsCorrect_ShouldReturnOkAndModel() throws Exception {

        when(groupService.saveGroup(any(GroupRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(responseDto)));
    }

    @Test
    void saveGroup_GroupWithoutGroupName_ShouldReturnBadRequestAndError() throws Exception {
        requestDto.setGroupName(null);

        postGroupAndExpectStatusAndErrorWithMessage(objectMapper.writeValueAsString(requestDto),
                400,
                "groupName is required");
    }

    @Test
    void saveGroup_GroupWithBlankGroupName_ShouldReturnBadRequestAndError() throws Exception {
        requestDto.setGroupName("");

        postGroupAndExpectStatusAndErrorWithMessage(objectMapper.writeValueAsString(requestDto),
                400,
                "groupName length should be between 1 and 50");
    }


    @Test
    void saveGroup_GroupWithLongGroupName_ShouldReturnBadRequestAndError() throws Exception {
        requestDto.setGroupName(RANDOM_51_STRING);

        postGroupAndExpectStatusAndErrorWithMessage(objectMapper.writeValueAsString(requestDto),
                400,
                "groupName length should be between 1 and 50");
    }

    @Test
    void saveGroup_GroupWithoutCurrency_ShouldReturnBadRequestAndError() throws Exception {
        requestDto.setCurrency(null);

        postGroupAndExpectStatusAndErrorWithMessage(objectMapper.writeValueAsString(requestDto),
                400,
                "currency is required");
    }

    @Test
    void saveGroup_GroupWithWrongCurrency_ShouldReturnBadRequestAndError() throws Exception {
        JSONObject json = new JSONObject(objectMapper.writeValueAsString(requestDto));
        json.put("currency", "TRY");

        postGroupAndExpectStatusAndErrorWithMessage(json.toString(),
                400,
                "Invalid currency value. Should be one of following : UAH, USD, EUR");
    }

    @Test
    void saveGroup_GroupWithoutMembers_ShouldReturnBadRequestAndError() throws Exception {
        requestDto.setMembers(null);

        postGroupAndExpectStatusAndErrorWithMessage(objectMapper.writeValueAsString(requestDto),
                400,
                "members is required");
    }

    @Test
    void saveGroup_GroupMembersIdsAreInvalid_ShouldReturnBadRequestAndErrorWithMsgFromException() throws Exception {
        Exception error = new GroupMembersIdsValidationException("msg");

        when(groupService.saveGroup(any(GroupRequestDto.class))).thenThrow(error);

        postGroupAndExpectStatusAndErrorWithMessage(objectMapper.writeValueAsString(requestDto),
                400,
                error.getMessage());
    }

    @Test
    void saveGroup_UserServiceError_ShouldReturnServerErrorAndErrorWithMsgFromException() throws Exception {
        Exception error = new UserServiceException("msg");

        when(groupService.saveGroup(any(GroupRequestDto.class))).thenThrow(error);

        postGroupAndExpectStatusAndErrorWithMessage(objectMapper.writeValueAsString(requestDto),
                500,
                error.getMessage());
    }

    private void postGroupAndExpectStatusAndErrorWithMessage(String content, int expectedStatus, String expectedMsg) throws Exception {
        String errorJson = mockMvc.perform(post("/groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().is(expectedStatus))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        ErrorDto error = objectMapper.readValue(errorJson, ErrorDto.class);

        assertEquals(error.getStatusCode(), expectedStatus);
        assertNotNull(error.getMessages());
        assertEquals(1, error.getMessages().size());
        assertEquals(expectedMsg, error.getMessages().get(0));
        assertNotNull(error.getTimestamp());
    }

    private static String RANDOM_51_STRING = "JZSfzulP8b9whnGwqRKuJZSfzulP8b9whnGwqRKuJZSfzulP8bq";
}