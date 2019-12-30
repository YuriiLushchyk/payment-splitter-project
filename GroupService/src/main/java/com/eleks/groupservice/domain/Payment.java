package com.eleks.groupservice.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    private Long id;
    private String paymentDescription;
    private Double price;
    private List<Long> coPayers;
    private Long creatorId;
    private Long groupId;
    private Instant timestamp;
}
