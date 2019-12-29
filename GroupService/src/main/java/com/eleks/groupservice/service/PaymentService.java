package com.eleks.groupservice.service;

import com.eleks.groupservice.dto.payment.PaymentRequestDto;
import com.eleks.groupservice.dto.payment.PaymentResponseDto;
import com.eleks.groupservice.exception.UsersIdsValidationException;

import java.util.List;
import java.util.Optional;

public interface PaymentService {
    PaymentResponseDto createPayment(Long groupId, Long creatorId, PaymentRequestDto requestDto) throws UsersIdsValidationException;

    Optional<PaymentResponseDto> getPayment(Long groupId, Long creatorId, Long paymentId);

    Optional<List<PaymentResponseDto>> getPayments(Long groupId);

    void deletePayment(Long groupId, Long paymentId);
}
