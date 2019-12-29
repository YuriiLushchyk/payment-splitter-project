package com.eleks.groupservice.controller;

import com.eleks.groupservice.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController {

    private PaymentService service;

    @Autowired
    public PaymentController(PaymentService service) {
        this.service = service;
    }
}
