package com.eleks.userservice.controller;

import com.eleks.userservice.advisor.ResponseExceptionHandler;
import com.eleks.userservice.dto.ErrorDto;
import com.eleks.userservice.dto.UserDto;
import com.eleks.userservice.exception.ResourceNotFoundException;
import com.eleks.userservice.exception.UserDataException;
import com.eleks.userservice.service.UserService;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static com.eleks.userservice.TestUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    private UserDto userData;

    @BeforeEach
    public void setUp() {
        mockMvc = standaloneSetup(controller)
                .setControllerAdvice(new ResponseExceptionHandler())
                .build();

        userData = UserDto.builder()
                .username("username")
                .firstName("firstName")
                .lastName("lastName")
                .dateOfBirth(LocalDate.now())
                .email("username@eleks.com")
                .receiveNotifications(true)
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
        assertEquals(error.getStatusCode(), HttpStatus.NOT_FOUND.value());
        assertEquals(1, error.getMessages().size());
        assertNotNull(error.getTimestamp());
    }

    @Test
    public void getUsers_UsersExists_ReturnListOfUsers() throws Exception {
        List<UserDto> list = Arrays.asList(UserDto.builder().id(1L).build(),
                UserDto.builder().id(2L).build(),
                UserDto.builder().id(3L).build());

        when(service.getUsers()).thenReturn(list);

        mockMvc.perform(get("/users/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(asJsonString(list)));
    }

    @Test
    public void createUser_UserDoesntExist_ReturnOKAndSavedUser() throws Exception {
        UserDto result = UserDto.builder()
                .id(1L)
                .username(userData.getUsername())
                .firstName(userData.getFirstName())
                .lastName(userData.getLastName())
                .dateOfBirth(userData.getDateOfBirth())
                .email(userData.getEmail())
                .receiveNotifications(userData.getReceiveNotifications())
                .build();

        when(service.saveUser(any(UserDto.class))).thenReturn(result);

        mockMvc.perform(post("/users/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userData)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(asJsonString(result)));
    }

    @Test
    public void createUser_WrongUserData_ReturnBabRequestAndError() throws Exception {

        when(service.saveUser(any(UserDto.class))).thenThrow(UserDataException.class);

        String responseBody = mockMvc.perform(post("/users/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userData)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        ErrorDto error = jsonAsObject(responseBody, ErrorDto.class);

        assertEquals(error.getStatusCode(), HttpStatus.BAD_REQUEST.value());
        assertNotNull(error.getMessages());
        assertEquals(1, error.getMessages().size());
        assertNotNull(error.getTimestamp());
    }

    @Test
    public void createUser_WithoutUsername_ReturnBabRequestAndError() throws Exception {
        userData.setUsername(null);
        postUserDataAndExpectBadRequestErrorWithSingleMsg(asJsonString(userData));
    }

    @Test
    public void createUser_WithBlankUsername_ReturnBabRequestAndError() throws Exception {
        userData.setUsername("");
        postUserDataAndExpectBadRequestErrorWithSingleMsg(asJsonString(userData));
    }

    @Test
    public void createUser_WithTooLongUsername_ReturnBabRequestAndError() throws Exception {
        userData.setUsername(getRandomString(51));
        postUserDataAndExpectBadRequestErrorWithSingleMsg(asJsonString(userData));
    }

    @Test
    public void createUser_WithoutFirstName_ReturnBabRequestAndError() throws Exception {
        userData.setFirstName(null);
        postUserDataAndExpectBadRequestErrorWithSingleMsg(asJsonString(userData));
    }

    @Test
    public void createUser_WithBlankFirstName_ReturnBabRequestAndError() throws Exception {
        userData.setFirstName("");
        postUserDataAndExpectBadRequestErrorWithSingleMsg(asJsonString(userData));
    }

    @Test
    public void createUser_WithTooLongFirstName_ReturnBabRequestAndError() throws Exception {
        userData.setFirstName(getRandomString(51));
        postUserDataAndExpectBadRequestErrorWithSingleMsg(asJsonString(userData));
    }

    @Test
    public void createUser_WithoutLastName_ReturnBabRequestAndError() throws Exception {
        userData.setLastName(null);
        postUserDataAndExpectBadRequestErrorWithSingleMsg(asJsonString(userData));
    }

    @Test
    public void createUser_WithBlankLastName_ReturnBabRequestAndError() throws Exception {
        userData.setLastName("");
        postUserDataAndExpectBadRequestErrorWithSingleMsg(asJsonString(userData));
    }

    @Test
    public void createUser_WithTooLongLastName_ReturnBabRequestAndError() throws Exception {
        userData.setLastName(getRandomString(51));
        postUserDataAndExpectBadRequestErrorWithSingleMsg(asJsonString(userData));
    }

    @Test
    public void createUser_WithoutBirthDate_ReturnBabRequestAndError() throws Exception {
        userData.setDateOfBirth(null);
        postUserDataAndExpectBadRequestErrorWithSingleMsg(asJsonString(userData));
    }

    @Test
    public void createUser_WithWrongDateFormat_ReturnBabRequestAndError() throws Exception {
        JSONObject json = new JSONObject(asJsonString(userData));
        json.put("dateOfBirth", "2019-12-19");
        postUserDataAndExpectBadRequestErrorWithSingleMsg(json.toString());
    }

    @Test
    public void createUser_WithoutEmail_ReturnBabRequestAndError() throws Exception {
        userData.setEmail(null);
        postUserDataAndExpectBadRequestErrorWithSingleMsg(asJsonString(userData));
    }

    @Test
    public void createUser_WithWrongEmailFormat_ReturnBabRequestAndError() throws Exception {
        JSONObject json = new JSONObject(asJsonString(userData));
        json.put("email", "#fr@gr@.com");
        postUserDataAndExpectBadRequestErrorWithSingleMsg(json.toString());
    }

    @Test
    public void createUser_WithoutNotificationProp_ReturnBabRequestAndError() throws Exception {
        userData.setReceiveNotifications(null);
        postUserDataAndExpectBadRequestErrorWithSingleMsg(asJsonString(userData));
    }

    @Test
    public void createUser_InvalidNotificationProp_ReturnBabRequestAndError() throws Exception {
        JSONObject json = new JSONObject(asJsonString(userData));
        json.put("receiveNotifications", "#fr@gr@.com");
        postUserDataAndExpectBadRequestErrorWithSingleMsg(json.toString());
    }

    private void postUserDataAndExpectBadRequestErrorWithSingleMsg(String content) throws Exception {
        String responseBody = mockMvc.perform(post("/users/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        ErrorDto error = jsonAsObject(responseBody, ErrorDto.class);

        assertEquals(error.getStatusCode(), HttpStatus.BAD_REQUEST.value());
        assertNotNull(error.getMessages());
        assertEquals(1, error.getMessages().size());
        assertNotNull(error.getTimestamp());
    }

    @Test
    public void editUser_UserExists_ReturnOkAndUpdatedData() throws Exception {
        UserDto result = UserDto.builder()
                .id(1L)
                .username(userData.getUsername())
                .firstName(userData.getFirstName())
                .lastName(userData.getLastName())
                .dateOfBirth(userData.getDateOfBirth())
                .email(userData.getEmail())
                .receiveNotifications(userData.getReceiveNotifications())
                .build();

        when(service.editUser(anyLong(), any(UserDto.class))).thenReturn(result);

        mockMvc.perform(put("/users/" + result.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userData)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(asJsonString(result)));
    }

    @Test
    public void editUser_UserDoesntExist_ReturnNotFoundAndError() throws Exception {

        when(service.editUser(anyLong(), any(UserDto.class))).thenThrow(ResourceNotFoundException.class);

        String responseBody = mockMvc.perform(put("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(userData)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        ErrorDto error = jsonAsObject(responseBody, ErrorDto.class);

        assertEquals(error.getStatusCode(), HttpStatus.NOT_FOUND.value());
        assertNotNull(error.getMessages());
        assertEquals(1, error.getMessages().size());
        assertNotNull(error.getTimestamp());
    }

    @Test
    public void editUser_WithoutUsername_ReturnBabRequestAndError() throws Exception {
        Long id = 1L;
        userData.setId(id);
        userData.setUsername(null);
        putUserDataAndExpectBadRequestErrorWithSingleMsg(id, asJsonString(userData));
    }

    @Test
    public void editUser_WithBlankUsername_ReturnBabRequestAndError() throws Exception {
        Long id = 1L;
        userData.setId(id);
        userData.setUsername("");
        putUserDataAndExpectBadRequestErrorWithSingleMsg(id, asJsonString(userData));
    }

    @Test
    public void editUser_WithTooLongUsername_ReturnBabRequestAndError() throws Exception {
        Long id = 1L;
        userData.setId(id);
        userData.setUsername(getRandomString(51));
        putUserDataAndExpectBadRequestErrorWithSingleMsg(id, asJsonString(userData));
    }

    @Test
    public void editUser_WithoutFirstName_ReturnBabRequestAndError() throws Exception {
        Long id = 1L;
        userData.setId(id);
        userData.setFirstName(null);
        putUserDataAndExpectBadRequestErrorWithSingleMsg(id, asJsonString(userData));
    }

    @Test
    public void editUser_WithBlankFirstName_ReturnBabRequestAndError() throws Exception {
        Long id = 1L;
        userData.setId(id);
        userData.setFirstName("");
        putUserDataAndExpectBadRequestErrorWithSingleMsg(id, asJsonString(userData));
    }

    @Test
    public void editUser_WithTooLongFirstName_ReturnBabRequestAndError() throws Exception {
        Long id = 1L;
        userData.setId(id);
        userData.setFirstName(getRandomString(51));
        putUserDataAndExpectBadRequestErrorWithSingleMsg(id, asJsonString(userData));
    }

    @Test
    public void editUser_WithoutLastName_ReturnBabRequestAndError() throws Exception {
        Long id = 1L;
        userData.setId(id);
        userData.setLastName(null);
        putUserDataAndExpectBadRequestErrorWithSingleMsg(id, asJsonString(userData));
    }

    @Test
    public void editUser_WithBlankLastName_ReturnBabRequestAndError() throws Exception {
        Long id = 1L;
        userData.setId(id);
        userData.setLastName("");
        putUserDataAndExpectBadRequestErrorWithSingleMsg(id, asJsonString(userData));
    }

    @Test
    public void editUser_WithTooLongLastName_ReturnBabRequestAndError() throws Exception {
        Long id = 1L;
        userData.setId(id);
        userData.setLastName(getRandomString(51));
        putUserDataAndExpectBadRequestErrorWithSingleMsg(id, asJsonString(userData));
    }

    @Test
    public void editUser_WithoutBirthDate_ReturnBabRequestAndError() throws Exception {
        Long id = 1L;
        userData.setId(id);
        userData.setDateOfBirth(null);
        putUserDataAndExpectBadRequestErrorWithSingleMsg(id, asJsonString(userData));
    }

    @Test
    public void editUser_WithWrongDateFormat_ReturnBabRequestAndError() throws Exception {
        Long id = 1L;
        userData.setId(id);
        JSONObject json = new JSONObject(asJsonString(userData));
        json.put("dateOfBirth", "2019-12-19");
        putUserDataAndExpectBadRequestErrorWithSingleMsg(id, json.toString());
    }

    @Test
    public void editUser_WithoutEmail_ReturnBabRequestAndError() throws Exception {
        Long id = 1L;
        userData.setId(id);
        userData.setEmail(null);
        putUserDataAndExpectBadRequestErrorWithSingleMsg(id, asJsonString(userData));
    }

    @Test
    public void editUser_WithWrongEmailFormat_ReturnBabRequestAndError() throws Exception {
        Long id = 1L;
        userData.setId(id);
        JSONObject json = new JSONObject(asJsonString(userData));
        json.put("email", "#fr@gr@.com");
        putUserDataAndExpectBadRequestErrorWithSingleMsg(id, json.toString());
    }

    @Test
    public void editUser_WithoutNotificationProp_ReturnBabRequestAndError() throws Exception {
        Long id = 1L;
        userData.setId(id);
        userData.setReceiveNotifications(null);
        putUserDataAndExpectBadRequestErrorWithSingleMsg(id, asJsonString(userData));
    }

    @Test
    public void editUser_InvalidNotificationProp_ReturnBabRequestAndError() throws Exception {
        Long id = 1L;
        userData.setId(id);
        JSONObject json = new JSONObject(asJsonString(userData));
        json.put("receiveNotifications", "#fr@gr@.com");
        putUserDataAndExpectBadRequestErrorWithSingleMsg(id, json.toString());
    }

    private void putUserDataAndExpectBadRequestErrorWithSingleMsg(Long id, String content) throws Exception {
        String responseBody = mockMvc.perform(put("/users/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        ErrorDto error = jsonAsObject(responseBody, ErrorDto.class);

        assertEquals(error.getStatusCode(), HttpStatus.BAD_REQUEST.value());
        assertNotNull(error.getMessages());
        assertEquals(1, error.getMessages().size());
        assertNotNull(error.getTimestamp());
    }

    @Test
    public void deleteUser_UserExists_ReturnOk() throws Exception {
        Long id = 1L;
        mockMvc.perform(delete("/users/" + id))
                .andExpect(status().isOk());

        verify(service).deleteUserById(eq(id));
    }

    @Test
    public void deleteUser_UserDoesntExist_ReturnNotFoundAndError() throws Exception {
        Long id = 1L;
        doThrow(ResourceNotFoundException.class).when(service).deleteUserById(id);

        String responseBody = mockMvc.perform(delete("/users/" + id))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();


        ErrorDto error = jsonAsObject(responseBody, ErrorDto.class);

        assertEquals(error.getStatusCode(), HttpStatus.NOT_FOUND.value());
        assertNotNull(error.getMessages());
        assertEquals(1, error.getMessages().size());
        assertNotNull(error.getTimestamp());
    }
}