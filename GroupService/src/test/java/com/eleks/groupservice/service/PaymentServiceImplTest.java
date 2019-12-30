package com.eleks.groupservice.service;

import com.eleks.groupservice.domain.Currency;
import com.eleks.groupservice.domain.Group;
import com.eleks.groupservice.domain.Payment;
import com.eleks.groupservice.dto.payment.PaymentRequestDto;
import com.eleks.groupservice.dto.payment.PaymentResponseDto;
import com.eleks.groupservice.exception.ResourceNotFoundException;
import com.eleks.groupservice.exception.UsersIdsValidationException;
import com.eleks.groupservice.repository.GroupRepository;
import com.eleks.groupservice.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @Mock
    GroupRepository groupRepo;

    @Mock
    PaymentRepository paymentRepo;

    @InjectMocks
    PaymentServiceImpl service;

    PaymentRequestDto paymentRequest;

    Group group;

    Payment payment;

    Long creatorId = 1L;

    @BeforeEach
    void setUp() {
        paymentRequest = PaymentRequestDto.builder()
                .paymentDescription("paymentDescription")
                .coPayers(Arrays.asList(1L, 2L))
                .price(100D)
                .build();

        group = Group.builder()
                .id(1L)
                .members(Arrays.asList(1L, 2L))
                .currency(Currency.UAH)
                .groupName("groupName")
                .build();

        payment = Payment.builder()
                .id(1L)
                .creatorId(creatorId)
                .group(group)
                .price(paymentRequest.getPrice())
                .coPayers(paymentRequest.getCoPayers())
                .paymentDescription(paymentRequest.getPaymentDescription())
                .timestamp(Instant.now())
                .build();
    }

    @Test
    void createPayment_GroupExistsCoPayersAreValid_SaveAndReturnResponseDto() {
        when(groupRepo.findById(group.getId())).thenReturn(Optional.of(group));

        when(paymentRepo.save(any(Payment.class))).thenReturn(payment);

        PaymentResponseDto response = service.createPayment(group.getId(), creatorId, paymentRequest);

        assertNotNull(response);
        assertNotNull(response.getId());
        assertNotNull(response.getGroupId());
        assertNotNull(response.getCreatorId());
        assertNotNull(response.getTimestamp());
        assertEquals(paymentRequest.getPaymentDescription(), response.getPaymentDescription());
        assertEquals(paymentRequest.getPrice(), response.getPrice());
        assertEquals(paymentRequest.getCoPayers(), response.getCoPayers());
    }

    @Test
    void createPayment_GroupDoesntExist_ThrowResourceNotFoundException() {
        when(groupRepo.findById(group.getId())).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> service.createPayment(group.getId(), creatorId, paymentRequest));

        assertEquals("Group doesn't exist", ex.getMessage());
    }

    @Test
    void createPayment_CoPayersIdsNotFromRequestedGroup_ThrowUsersIdsValidationException() {
        when(groupRepo.findById(group.getId())).thenReturn(Optional.of(group));
        paymentRequest.setCoPayers(Arrays.asList(42L, 24L));

        UsersIdsValidationException ex = assertThrows(UsersIdsValidationException.class,
                () -> service.createPayment(group.getId(), creatorId, paymentRequest));

        assertEquals("Payment co-payers are not members of group", ex.getMessage());
    }

    @Test
    void getPayment_GroupAndPaymentExists_ShouldReturnResponseModel() {
        group.getPayments().add(payment);

        when(groupRepo.findById(group.getId())).thenReturn(Optional.of(group));

        Optional<PaymentResponseDto> result = service.getPayment(group.getId(), payment.getId());

        assertTrue(result.isPresent());
    }

    @Test
    void getPayment_GroupDoesntExist_ShouldReturnEmptyOptional() {
        when(groupRepo.findById(group.getId())).thenReturn(Optional.empty());

        Optional<PaymentResponseDto> result = service.getPayment(group.getId(), payment.getId());

        assertFalse(result.isPresent());
    }

    @Test
    void getPayment_GroupExistsButPaymentIsNot_ShouldReturnEmptyOptional() {
        group.setPayments(Collections.emptySet());

        when(groupRepo.findById(group.getId())).thenReturn(Optional.of(group));

        Optional<PaymentResponseDto> result = service.getPayment(group.getId(), payment.getId());

        assertFalse(result.isPresent());
    }

}