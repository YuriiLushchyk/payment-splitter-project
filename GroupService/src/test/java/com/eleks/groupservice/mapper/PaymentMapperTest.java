package com.eleks.groupservice.mapper;

import com.eleks.groupservice.domain.Payment;
import com.eleks.groupservice.dto.payment.PaymentRequestDto;
import com.eleks.groupservice.dto.payment.PaymentResponseDto;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class PaymentMapperTest {

    @Test
    void toEntity() {
        Long creatorId = 1L;
        Long groupId = 2L;
        PaymentRequestDto dto = PaymentRequestDto.builder()
                .paymentDescription("paymentDescription")
                .coPayers(Arrays.asList(1L, 2L, 3L))
                .price(100.5)
                .build();

        Payment entity = PaymentMapper.toEntity(groupId, creatorId, dto);

        assertNotNull(entity);
        assertNull(entity.getId());
        assertNull(entity.getTimestamp());
        assertEquals(dto.getPaymentDescription(), entity.getPaymentDescription());
        assertEquals(dto.getCoPayers(), entity.getCoPayers());
        assertEquals(dto.getPrice(), entity.getPrice());
    }

    @Test
    void toDto() {
        Payment entity = Payment.builder()
                .id(1L)
                .creatorId(2L)
                .groupId(3L)
                .paymentDescription("paymentDescription")
                .price(200.5)
                .coPayers(Arrays.asList(1L, 2L, 3L))
                .timestamp(Instant.now())
                .build();

        PaymentResponseDto dto = PaymentMapper.toDto(entity);

        assertNotNull(dto);
        assertEquals(entity.getId(), dto.getId());
        assertEquals(entity.getCreatorId(), dto.getCreatorId());
        assertEquals(entity.getGroupId(), dto.getGroupId());
        assertEquals(entity.getPaymentDescription(), dto.getPaymentDescription());
        assertEquals(entity.getCoPayers(), dto.getCoPayers());
        assertEquals(entity.getPrice(), dto.getPrice());
        assertEquals(entity.getTimestamp(), dto.getTimestamp());
    }
}