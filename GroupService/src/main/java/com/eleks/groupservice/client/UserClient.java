package com.eleks.groupservice.client;

import com.eleks.common.security.SecurityPrincipalHolder;
import com.eleks.common.security.model.LoggedInUserPrincipal;
import com.eleks.groupservice.dto.userclient.UserDto;
import com.eleks.groupservice.dto.userclient.UserSearchDto;
import com.eleks.groupservice.exception.UserServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
public class UserClient {
    private final String baseUrl;
    private RestTemplate restTemplate;
    private SecurityPrincipalHolder principalHolder;

    @Autowired
    public UserClient(RestTemplate restTemplate, @Value("${userservice.url}") String baseUrl, SecurityPrincipalHolder principalHolder) {
        this.baseUrl = baseUrl;
        this.restTemplate = restTemplate;
        this.principalHolder = principalHolder;
    }

    public boolean areUserIdsValid(Set<Long> userIds) throws UserServiceException {
        try {
            return getUsersFromUserService(userIds).size() == userIds.size();
        } catch (HttpClientErrorException ex) {
            log.info("Client error during request to UserService", ex);
            return false;
        }
    }

    public List<UserDto> getUsersByIds(Set<Long> userIds) throws UserServiceException {
        try {
            return getUsersFromUserService(userIds);
        } catch (HttpClientErrorException ex) {
            log.info("Client error during request to UserService", ex);
            return Collections.emptyList();
        }
    }

    private List<UserDto> getUsersFromUserService(Set<Long> userIds) throws HttpClientErrorException, UserServiceException {
        try {
            String url = baseUrl + "/users/search";
            UserSearchDto requestDto = new UserSearchDto(userIds);
            HttpEntity<UserSearchDto> requestEntity = new HttpEntity<>(requestDto, getHeaders());

            UserDto[] responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, UserDto[].class).getBody();
            return responseEntity == null ? Collections.emptyList() : Arrays.asList(responseEntity);
        } catch (HttpServerErrorException ex) {
            log.info("Server error during request to UserService");
            throw new UserServiceException("Server error during request to UserService");
        }
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        LoggedInUserPrincipal principal = principalHolder.getPrincipal();
        headers.setBearerAuth(principal.getJwt());
        return headers;
    }
}
