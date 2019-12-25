package com.eleks.groupservice.client;

import com.eleks.groupservice.dto.UserDto;
import com.eleks.groupservice.dto.UserSearchDto;
import com.eleks.groupservice.exception.UserServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

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

    public boolean areUserIdsValid(List<Long> userIds) {
        try {
            return validateOnUserService(userIds);
        } catch (HttpClientErrorException ex) {
            log.info("Client error during request to UserService");
            return false;
        } catch (HttpServerErrorException ex) {
            log.info("Server error during request to UserService");
            throw new UserServiceException("Server error during request to UserService");
        }
    }

    private boolean validateOnUserService(List<Long> userIds) throws RestClientException {
        String url = baseUrl + "/users/search";
        UserSearchDto requestDto = new UserSearchDto(userIds);
        UserDto[] responseEntity = restTemplate.postForEntity(url, requestDto, UserDto[].class).getBody();
        return responseEntity != null && responseEntity.length == userIds.size();
    }
}
