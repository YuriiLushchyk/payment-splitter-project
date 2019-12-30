package com.eleks.groupservice.service;

import com.eleks.groupservice.domain.Group;
import com.eleks.groupservice.domain.Payment;
import com.eleks.groupservice.dto.payment.PaymentRequestDto;
import com.eleks.groupservice.dto.payment.PaymentResponseDto;
import com.eleks.groupservice.exception.ResourceNotFoundException;
import com.eleks.groupservice.exception.UsersIdsValidationException;
import com.eleks.groupservice.mapper.PaymentMapper;
import com.eleks.groupservice.repository.GroupRepository;
import com.eleks.groupservice.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PaymentServiceImpl implements PaymentService {

    private GroupRepository groupRepo;
    private PaymentRepository paymentRepo;

    public PaymentServiceImpl(GroupRepository groupRepo, PaymentRepository paymentRepo) {
        this.groupRepo = groupRepo;
        this.paymentRepo = paymentRepo;
    }

    @Override
    @Transactional
    public PaymentResponseDto createPayment(Long groupId, Long creatorId, PaymentRequestDto requestDto) throws ResourceNotFoundException, UsersIdsValidationException {
        Group group = groupRepo.findById(groupId).orElseThrow(() -> new ResourceNotFoundException("Group doesn't exist"));

        boolean areCoPayersIdsValid = group.getMembers().containsAll(requestDto.getCoPayers());

        if (!areCoPayersIdsValid) {
            throw new UsersIdsValidationException("Payment co-payers are not members of group");
        }

        Payment payment = PaymentMapper.toEntity(creatorId, group, requestDto);
        return PaymentMapper.toDto(paymentRepo.save(payment));
    }

    @Override
    public Optional<PaymentResponseDto> getPayment(Long groupId, Long paymentId) {
        Optional<Group> groupResult = groupRepo.findById(groupId);
        if (groupResult.isPresent()) {
            return groupResult.get()
                    .getPayments()
                    .stream()
                    .filter(payment -> payment.getId().equals(paymentId))
                    .findFirst()
                    .map(PaymentMapper::toDto);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<List<PaymentResponseDto>> getPayments(Long groupId) {
        Optional<Group> groupResult = groupRepo.findById(groupId);
        if (groupResult.isPresent()) {
            List<PaymentResponseDto> result = groupResult.get()
                    .getPayments()
                    .stream()
                    .map(PaymentMapper::toDto)
                    .collect(Collectors.toList());
            return Optional.of(result);
        } else {
            return Optional.empty();
        }
    }

    @Override
    @Transactional
    public void deletePayment(Long groupId, Long paymentId) throws ResourceNotFoundException {

    }
}
