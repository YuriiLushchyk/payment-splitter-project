package com.eleks.groupservice.dto.userclient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSearchDto {
    private Set<Long> userIds;
}
