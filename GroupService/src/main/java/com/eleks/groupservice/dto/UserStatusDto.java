package com.eleks.groupservice.dto;

import com.eleks.groupservice.domain.Currency;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserStatusDto {

    private Long userId;
    private String username;
    private Currency currency;
    private Double value;
}
