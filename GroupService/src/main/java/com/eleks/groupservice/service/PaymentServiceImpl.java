package com.eleks.groupservice.service;

import com.eleks.groupservice.dto.payment.PaymentRequestDto;
import com.eleks.groupservice.dto.payment.PaymentResponseDto;
import com.eleks.groupservice.exception.ResourceNotFoundException;
import com.eleks.groupservice.exception.UsersIdsValidationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Override
    public PaymentResponseDto createPayment(Long groupId, Long creatorId, PaymentRequestDto requestDto) throws ResourceNotFoundException, UsersIdsValidationException {
        return null;
    }

    @Override
    public Optional<PaymentResponseDto> getPayment(Long groupId, Long creatorId, Long paymentId) {
        return Optional.empty();
    }

    @Override
    public Optional<List<PaymentResponseDto>> getPayments(Long groupId) {
        return Optional.empty();
    }

    @Override
    public void deletePayment(Long groupId, Long paymentId) throws ResourceNotFoundException {

    }
}
