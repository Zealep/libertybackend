package com.zealepsoluciones.libertybackend.service.impl;

import com.zealepsoluciones.libertybackend.model.dto.CustomerDTO;
import com.zealepsoluciones.libertybackend.model.dto.LoanDTO;
import com.zealepsoluciones.libertybackend.model.entity.Installment;
import com.zealepsoluciones.libertybackend.model.entity.Loan;
import com.zealepsoluciones.libertybackend.model.enums.LoanStatus;
import com.zealepsoluciones.libertybackend.repository.InstallmentRepository;
import com.zealepsoluciones.libertybackend.repository.LoanRepository;
import com.zealepsoluciones.libertybackend.service.InstallmentService;
import com.zealepsoluciones.libertybackend.service.LoanService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository;
    private final InstallmentRepository installmentRepository;
    private final InstallmentService installmentService;
    public LoanServiceImpl(LoanRepository loanRepository, InstallmentRepository installmentRepository, InstallmentService installmentService) {
        this.loanRepository = loanRepository;
        this.installmentRepository = installmentRepository;
        this.installmentService = installmentService;
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
    public Loan getById(Long loanId) {
        return loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found"));
    }

    @Override
    public List<LoanDTO> getAllLoans() {
        return ((List<Loan>) loanRepository.findAll()).stream()
                .map(loan -> new LoanDTO(
                        loan.getId(),
                        loan.getPrincipal(),
                        loan.getMonthlyInterestRate(),
                        loan.getTermMonths(),
                        loan.getInterestType(),
                        loan.getStatus(),
                        loan.getDisbursementDate(),
                        new CustomerDTO(
                                loan.getCustomer().getId(),
                                loan.getCustomer().getFirstName(),
                                loan.getCustomer().getLastName(),
                                loan.getCustomer().getDocumentNumber(),
                                loan.getCustomer().getEmail(),
                                loan.getCustomer().getPhone(),
                                loan.getCustomer().getState()
                        )
                ))
                .toList();
    }

    @Override
    public Loan updateLoan(Loan loan) {
        return null;
    }

    @Override
    public Loan cancelLoan(Long loanId) {
        Loan loan = getById(loanId);
        if (loan.getStatus() == LoanStatus.CLOSED) {
            throw new RuntimeException("No se puede cancelar el prestamo cerrado");
        }
        loan.setStatus(LoanStatus.CANCELLED);
        return loanRepository.save(loan);
    }

    @Override
    public Loan updateLoanStatus(Long loanId, LoanStatus status) {
        Loan loan = getById(loanId);
        loan.setStatus(status);
        return loanRepository.save(loan);
    }

    @Override
    public void deleteLoan(Long loanId) {
        loanRepository.deleteById(loanId);
    }


}
