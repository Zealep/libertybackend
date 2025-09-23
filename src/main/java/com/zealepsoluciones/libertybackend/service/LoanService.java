package com.zealepsoluciones.libertybackend.service;

import com.zealepsoluciones.libertybackend.model.dto.LoanDTO;
import com.zealepsoluciones.libertybackend.model.entity.Loan;
import com.zealepsoluciones.libertybackend.model.enums.LoanStatus;

import java.util.List;

public interface LoanService {
    Loan createLoan(Loan loan);
    Loan getById(Long loanId);
    List<LoanDTO> getAllLoans();
    Loan updateLoan(Loan loan);
    Loan cancelLoan(Long loanId);
    Loan updateLoanStatus(Long loanId, LoanStatus status);
    void deleteLoan(Long loanId);
}
