package com.zealepsoluciones.libertybackend.service.impl;

import com.zealepsoluciones.libertybackend.model.entity.Installment;
import com.zealepsoluciones.libertybackend.model.entity.Payment;
import com.zealepsoluciones.libertybackend.model.enums.InstallmentStatus;
import com.zealepsoluciones.libertybackend.model.enums.PaymentType;
import com.zealepsoluciones.libertybackend.repository.InstallmentRepository;
import com.zealepsoluciones.libertybackend.repository.PaymentRepository;
import com.zealepsoluciones.libertybackend.service.PaymentService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class PaymentServiceImpl implements PaymentService {
    private final InstallmentRepository installmentRepository;
    private final PaymentRepository paymentRepository;

    public PaymentServiceImpl(InstallmentRepository installmentRepository,
                          PaymentRepository paymentRepository) {
        this.installmentRepository = installmentRepository;
        this.paymentRepository = paymentRepository;
    }

    @Override
    public Payment registerPayment(Long installmentId, BigDecimal amount, PaymentType type) {
        Installment installment = installmentRepository.findById(installmentId)
                .orElseThrow(() -> new RuntimeException("Installment not found"));

        Payment payment = new Payment();
        payment.setInstallment(installment);
        payment.setPaymentDate(LocalDate.now());
        payment.setAmount(amount);
        payment.setType(type);

        paymentRepository.save(payment);

        // Actualizar saldo de la cuota
        BigDecimal totalPaid = installment.getPayments().stream()
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (totalPaid.compareTo(installment.getAmount()) >= 0) {
            installment.setStatus(InstallmentStatus.PAID);
        } else if (LocalDate.now().isAfter(installment.getDueDate())) {
            installment.setStatus(InstallmentStatus.LATE);
        } else {
            installment.setStatus(InstallmentStatus.PENDING);
        }

        installmentRepository.save(installment);

        return payment;
    }

    @Override
    public void deletePayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        paymentRepository.delete(payment);
    }

    @Override
    public Payment updatePayment(Long paymentId, BigDecimal newAmount, LocalDate newPaymentDate) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        Installment installment = payment.getInstallment();

        // revertir el pago anterior
        installment.setRemainingBalance(installment.getRemainingBalance().add(payment.getAmount()));

        // aplicar el nuevo pago
        payment.setAmount(newAmount);
        payment.setPaymentDate(newPaymentDate);

        installment.setRemainingBalance(installment.getRemainingBalance().subtract(newAmount));

        installmentRepository.save(installment);
        return paymentRepository.save(payment);
    }
}
