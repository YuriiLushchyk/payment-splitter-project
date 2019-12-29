package com.eleks.groupservice.controller;

import com.eleks.groupservice.dto.payment.PaymentRequestDto;
import com.eleks.groupservice.dto.payment.PaymentResponseDto;
import com.eleks.groupservice.exception.ResourceNotFoundException;
import com.eleks.groupservice.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class PaymentController {
    public static final Long TEST_USER_ID = 1L;

    private PaymentService service;

    @Autowired
    public PaymentController(PaymentService service) {
        this.service = service;
    }

    @PostMapping("/groups/{groupId}/payments")
    public PaymentResponseDto createPayment(@PathVariable Long groupId, @Valid @RequestBody PaymentRequestDto requestDto) {
        return service.createPayment(groupId, TEST_USER_ID, requestDto);
    }

    @GetMapping("/groups/{groupId}/payments/{paymentId}")
    public PaymentResponseDto getPayment(@PathVariable Long groupId, @PathVariable Long paymentId) {
        return service.getPayment(groupId, TEST_USER_ID, paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("payment does't exist"));
    }

    @GetMapping("/groups/{groupId}/payments")
    public List<PaymentResponseDto> getPayments(@PathVariable Long groupId) {
        return service.getPayments(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("group does't exist"));
    }

    @DeleteMapping("/groups/{groupId}/payments/{paymentId}")
    public void deletePayment(@PathVariable Long groupId, @PathVariable Long paymentId) {
        service.deletePayment(groupId, paymentId);
    }
}
