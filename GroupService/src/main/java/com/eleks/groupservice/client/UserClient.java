package com.eleks.groupservice.client;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserClient {
    public boolean areUserIdsValid(List<Long> userIds) {
        return true;
    }
}
