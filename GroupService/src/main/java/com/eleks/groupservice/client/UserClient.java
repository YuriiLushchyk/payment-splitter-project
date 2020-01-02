package com.eleks.groupservice.client;

import com.eleks.groupservice.dto.userclient.UserDto;
import com.eleks.groupservice.dto.userclient.UserSearchDto;
import com.eleks.groupservice.exception.UserServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class UserClient {
    private final String baseUrl;
    private RestTemplate restTemplate;

    @Autowired
    public UserClient(RestTemplate restTemplate, @Value("${userservice.url}") String baseUrl) {
        this.baseUrl = baseUrl;
        this.restTemplate = restTemplate;
    }

    public boolean areUserIdsValid(List<Long> userIds) throws UserServiceException {
        try {
            return getUsersFromUserService(userIds).size() == userIds.size();
        } catch (HttpClientErrorException ex) {
            log.info("Client error during request to UserService");
            return false;
        }
    }

    public List<UserDto> getUsersByIds(List<Long> userIds) throws UserServiceException {
        try {
            return getUsersFromUserService(userIds);
        } catch (HttpClientErrorException ex) {
            log.info("Client error during request to UserService");
            return Collections.emptyList();
        }
    }

    private List<UserDto> getUsersFromUserService(List<Long> userIds) throws HttpClientErrorException, UserServiceException {
        try {
            String url = baseUrl + "/users/search";
            UserSearchDto requestDto = new UserSearchDto(userIds);
            UserDto[] responseEntity = restTemplate.postForEntity(url, requestDto, UserDto[].class).getBody();
            return responseEntity == null ? Collections.emptyList() : Arrays.asList(responseEntity);
        } catch (HttpServerErrorException ex) {
            log.info("Server error during request to UserService");
            throw new UserServiceException("Server error during request to UserService");
        }
    }
}
