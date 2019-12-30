package com.eleks.groupservice.mapper;

import com.eleks.groupservice.domain.Payment;
import com.eleks.groupservice.dto.payment.PaymentRequestDto;
import com.eleks.groupservice.dto.payment.PaymentResponseDto;

import java.util.Objects;

public class PaymentMapper {

    public static Payment toEntity(Long groupId, Long creatorId, PaymentRequestDto dto) {
        if (Objects.isNull(dto)) {
            return null;
        } else {
            return Payment.builder()
                    .paymentDescription(dto.getPaymentDescription())
                    .price(dto.getPrice())
                    .coPayers(dto.getCoPayers())
                    .creatorId(creatorId)
                    .groupId(groupId)
                    .build();
        }
    }

    public static PaymentResponseDto toDto(Payment entity) {
        if (Objects.isNull(entity)) {
            return null;
        } else {
            return PaymentResponseDto.builder()
                    .id(entity.getId())
                    .paymentDescription(entity.getPaymentDescription())
                    .price(entity.getPrice())
                    .coPayers(entity.getCoPayers())
                    .creatorId(entity.getCreatorId())
                    .groupId(entity.getGroupId())
                    .timestamp(entity.getTimestamp())
                    .build();
        }
    }
}
