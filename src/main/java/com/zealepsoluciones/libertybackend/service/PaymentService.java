package com.zealepsoluciones.libertybackend.service;

import com.zealepsoluciones.libertybackend.model.entity.Payment;
import com.zealepsoluciones.libertybackend.model.enums.PaymentType;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface PaymentService {
    Payment registerPayment(Long installmentId, BigDecimal amount, PaymentType type);
    void deletePayment(Long paymentId);
    Payment updatePayment(Long paymentId, BigDecimal newAmount, LocalDate newPaymentDate);
}
