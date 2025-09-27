package com.zealepsoluciones.libertybackend.controller;

import com.zealepsoluciones.libertybackend.model.entity.Payment;
import com.zealepsoluciones.libertybackend.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<Payment> processPayments(@RequestBody Payment payment) {
        return ResponseEntity.ok(paymentService.registerPayment(payment));
    }

    @GetMapping("/loan/{loanId}")
    public ResponseEntity<List<Payment>> listPaymentsByLoanId(@PathVariable Long loanId) {
        return ResponseEntity.ok(paymentService.getPaymentsByLoanId(loanId));
    }



}
