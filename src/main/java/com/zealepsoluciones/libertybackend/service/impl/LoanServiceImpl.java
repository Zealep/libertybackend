package com.zealepsoluciones.libertybackend.service.impl;

import com.zealepsoluciones.libertybackend.model.entity.Installment;
import com.zealepsoluciones.libertybackend.model.entity.Loan;
import com.zealepsoluciones.libertybackend.model.enums.InstallmentStatus;
import com.zealepsoluciones.libertybackend.model.enums.InterestType;
import com.zealepsoluciones.libertybackend.model.enums.LoanStatus;
import com.zealepsoluciones.libertybackend.repository.InstallmentRepository;
import com.zealepsoluciones.libertybackend.repository.LoanRepository;
import com.zealepsoluciones.libertybackend.service.InstallmentService;
import com.zealepsoluciones.libertybackend.service.LoanService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository;
    private final InstallmentRepository installmentRepository;
    private final InstallmentService installmentService;
    public LoanServiceImpl(LoanRepository loanRepository, InstallmentRepository installmentRepository) {
        this.loanRepository = loanRepository;
        this.installmentRepository = installmentRepository;
        this.installmentService = new InstallmentServiceImpl();
    }


    @Override
    public Loan createLoan(Loan loan) {
        // Primero guardar el préstamo
        Loan savedLoan = loanRepository.save(loan);

        // Generar cronograma de cuotas según el tipo de interés
        List<Installment> installments = installmentService.generateInstallments(savedLoan);

        // Asociar cada cuota con el préstamo
        installments.forEach(inst -> inst.setLoan(savedLoan));

        // Guardar todas las cuotas
        installmentRepository.saveAll(installments);

        // Asignar cuotas al préstamo para devolverlo completo
        savedLoan.setInstallments(installments);

        return savedLoan;
    }

    @Override
    public Loan getLoan(Long loanId) {
        return loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found"));
    }

    @Override
    public List<Loan> getAllLoans() {
        return (List<Loan>)loanRepository.findAll();
    }

    @Override
    public Loan cancelLoan(Long loanId) {
        Loan loan = getLoan(loanId);
        if (loan.getStatus() == LoanStatus.CLOSED) {
            throw new RuntimeException("No se puede cancelar el prestamo cerrado");
        }
        loan.setStatus(LoanStatus.CANCELLED);
        return loanRepository.save(loan);
    }

    @Override
    public Loan updateLoanStatus(Long loanId, LoanStatus status) {
        Loan loan = getLoan(loanId);
        loan.setStatus(status);
        return loanRepository.save(loan);
    }

    @Override
    public void deleteLoan(Long loanId) {
        loanRepository.deleteById(loanId);
    }


}
