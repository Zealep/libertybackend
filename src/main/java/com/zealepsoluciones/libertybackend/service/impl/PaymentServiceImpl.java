package com.zealepsoluciones.libertybackend.service.impl;

import com.zealepsoluciones.libertybackend.model.entity.Installment;
import com.zealepsoluciones.libertybackend.model.entity.Loan;
import com.zealepsoluciones.libertybackend.model.entity.Payment;
import com.zealepsoluciones.libertybackend.model.enums.InstallmentStatus;
import com.zealepsoluciones.libertybackend.model.enums.LoanStatus;
import com.zealepsoluciones.libertybackend.model.enums.PaymentType;
import com.zealepsoluciones.libertybackend.repository.InstallmentRepository;
import com.zealepsoluciones.libertybackend.repository.LoanRepository;
import com.zealepsoluciones.libertybackend.repository.PaymentRepository;
import com.zealepsoluciones.libertybackend.service.InstallmentService;
import com.zealepsoluciones.libertybackend.service.LoanService;
import com.zealepsoluciones.libertybackend.service.PaymentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final InstallmentRepository installmentRepository;
    private final PaymentRepository paymentRepository;
    private final InstallmentService installmentService;
    private final LoanRepository loanRepository;


    @Override
    @Transactional
    public Payment registerPayment(Payment payment) {

        Payment p = paymentRepository.save(payment);

        // Actualizar saldo de la cuota
        Installment installment = installmentService.getInstallmentById(p.getInstallment().getId());
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

        Loan loan = installment.getLoan();
        boolean allPaid = loan.getInstallments().stream()
                .allMatch(i -> i.getStatus() == InstallmentStatus.PAID);

        if (allPaid) {
            loan.setStatus(LoanStatus.CLOSED); // O el estado que corresponda
            // Asumiendo que tienes un LoanRepository
            loanRepository.save(loan);
        }

        return p;
    }

    @Override
    public void deletePayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        paymentRepository.delete(payment);
    }

    @Override
    public Payment updatePayment(Payment payment) {
        return null;
    }

    @Override
    public List<Payment> getAllPayments() {
        return List.of();
    }

    @Override
    public List<Payment> getPaymentsByInstallmentId(Long installmentId) {
        return List.of();
    }

    @Override
    public Payment getPaymentById(Long paymentId) {
        return null;
    }

    @Override
    public List<Payment> getPaymentsByLoanId(Long loanId) {
        return paymentRepository.findPaymentsByLoanId(loanId);
    }
}
