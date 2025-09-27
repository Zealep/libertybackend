package com.zealepsoluciones.libertybackend.service;

import com.zealepsoluciones.libertybackend.model.entity.Payment;
import com.zealepsoluciones.libertybackend.model.enums.PaymentType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface PaymentService {
    Payment registerPayment(Payment payment);
    void deletePayment(Long paymentId);
    Payment updatePayment(Payment payment);
    List<Payment> getAllPayments();
    List<Payment> getPaymentsByInstallmentId(Long installmentId);
    Payment getPaymentById(Long paymentId);
    List<Payment> getPaymentsByLoanId(Long loanId);
}
