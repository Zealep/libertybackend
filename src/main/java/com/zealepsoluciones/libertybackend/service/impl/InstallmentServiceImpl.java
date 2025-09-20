package com.zealepsoluciones.libertybackend.service.impl;

import com.zealepsoluciones.libertybackend.model.entity.Installment;
import com.zealepsoluciones.libertybackend.model.entity.Loan;
import com.zealepsoluciones.libertybackend.model.enums.InstallmentStatus;
import com.zealepsoluciones.libertybackend.model.enums.InterestType;
import com.zealepsoluciones.libertybackend.service.InstallmentService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class InstallmentServiceImpl implements InstallmentService {
    @Override
    public List<Installment> generateInstallments(
            Loan loan
    ) {
        if (loan.getInterestType() == InterestType.SIMPLE) {
            return generateSimpleInterestInstallments(loan);
        } else {
            return generateFrenchInterestInstallments(loan);
        }
    }

    /**
     * Generate installments with Simple Interest
     * Formula: Total = Principal + (Principal * rate * time)
     */
    private List<Installment> generateSimpleInterestInstallments(Loan loan) {
        List<Installment> installments = new ArrayList<>();

        BigDecimal monthlyRate = loan.getMonthlyInterestRate()
                .divide(BigDecimal.valueOf(100), 6, RoundingMode.HALF_UP);

        BigDecimal totalInterest = loan.getPrincipal()
                .multiply(monthlyRate)
                .multiply(BigDecimal.valueOf(loan.getTermMonths()));

        BigDecimal totalPayable = loan.getPrincipal().add(totalInterest);

        BigDecimal installmentAmount = totalPayable
                .divide(BigDecimal.valueOf(loan.getTermMonths()), 2, RoundingMode.HALF_UP);

        BigDecimal remaining = loan.getPrincipal();

        for (int i = 1; i <= loan.getTermMonths(); i++) {
            Installment inst = new Installment();
            inst.setNumber(i);
            inst.setDueDate(loan.getDisbursementDate().plusMonths(i));

            // interés fijo cada mes con tasa decimal
            BigDecimal interest = loan.getPrincipal()
                    .multiply(monthlyRate)
                    .setScale(2, RoundingMode.HALF_UP);

            BigDecimal principalPart = installmentAmount.subtract(interest);
            remaining = remaining.subtract(principalPart);

            inst.setAmount(installmentAmount);
            inst.setInterest(interest);
            inst.setPrincipalPart(principalPart);

            if (i == loan.getTermMonths()) {
                inst.setRemainingBalance(BigDecimal.ZERO);
            } else {
                inst.setRemainingBalance(remaining.max(BigDecimal.ZERO));
            }

            inst.setStatus(InstallmentStatus.PENDING);
            inst.setLoan(loan);

            installments.add(inst);
        }

        return installments;
    }

    /**
     * Generate installments with French System (Compound interest, fixed installment)
     * Formula:
     * installment = P * [ i * (1+i)^n ] / [ (1+i)^n - 1 ]
     */
    private List<Installment> generateFrenchInterestInstallments(Loan loan) {
        List<Installment> installments = new ArrayList<>();

        int n = loan.getTermMonths();
        BigDecimal P = loan.getPrincipal();

        // 🔹 Conversión: si viene como 1.7 -> 0.017
        BigDecimal i = loan.getMonthlyInterestRate().divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP);

        // Variables para la fórmula
        double principal = P.doubleValue();
        double rate = i.doubleValue();
        double pow = Math.pow(1 + rate, n);

        // Cuota fija francesa
        double installmentValue = principal * (rate * pow) / (pow - 1);

        BigDecimal fixedInstallment = BigDecimal.valueOf(installmentValue)
                .setScale(2, RoundingMode.HALF_UP);

        LocalDate dueDate = loan.getDisbursementDate();
        BigDecimal remaining = P.setScale(10, RoundingMode.HALF_UP);

        for (int k = 1; k <= n; k++) {
            Installment inst = new Installment();
            inst.setNumber(k);
            inst.setDueDate(dueDate.plusMonths(k));

            BigDecimal interest = remaining.multiply(i).setScale(2, RoundingMode.HALF_UP);
            BigDecimal principalPart = fixedInstallment.subtract(interest);

            // Ajustar última cuota
            if (k == n) {
                principalPart = remaining;
            }

            remaining = remaining.subtract(principalPart).setScale(10, RoundingMode.HALF_UP);

            inst.setAmount(fixedInstallment);
            inst.setInterest(interest);
            inst.setPrincipalPart(principalPart.setScale(2, RoundingMode.HALF_UP));
            inst.setRemainingBalance(remaining.max(BigDecimal.ZERO).setScale(2, RoundingMode.HALF_UP));
            inst.setStatus(InstallmentStatus.PENDING);
            inst.setLoan(loan);

            installments.add(inst);
        }

        return installments;
    }

}
