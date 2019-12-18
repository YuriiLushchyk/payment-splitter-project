package com.eleks.userservice.controller;

import com.eleks.userservice.advisor.ResponseExceptionHandler;
import com.eleks.userservice.dto.ErrorDto;
import com.eleks.userservice.dto.UserDto;
import com.eleks.userservice.exception.ResourceNotFoundException;
import com.eleks.userservice.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static com.eleks.userservice.TestUtils.asJsonString;
import static com.eleks.userservice.TestUtils.jsonAsObject;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@WebMvcTest
public class UserControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private UserService service;

    @Autowired
    private UserController controller;

    @BeforeEach
    public void setUp() {
        mockMvc = standaloneSetup(controller)
                .setControllerAdvice(new ResponseExceptionHandler())
                .build();
    }

    @Test
    public void getUser_userExits_ReturnOK() throws Exception {
        UserDto result = UserDto.builder()
                .id(1L)
                .username("username")
                .firstName("firstName")
                .lastName("lastName")
                .dateOfBirth(LocalDate.now())
                .email("username@eleks.com")
                .receiveNotifications(true)
                .build();

        when(service.getUser(result.getId())).thenReturn(result);

        mockMvc.perform(get("/users/" + result.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(asJsonString(result)));
    }


    @Test
    public void getUser_userDoesntExits_ReturnNotFoundAndError() throws Exception {
        Long id = 1L;
        when(service.getUser(id)).thenThrow(ResourceNotFoundException.class);

        String responseBody = mockMvc.perform(get("/users/" + id))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse().getContentAsString();

        ErrorDto error = jsonAsObject(responseBody, ErrorDto.class);
        assertEquals(error.getStatus(), HttpStatus.NOT_FOUND.value());
        assertNotNull(error.getMessage());
        assertNotNull(error.getTimestamp());
    }

}