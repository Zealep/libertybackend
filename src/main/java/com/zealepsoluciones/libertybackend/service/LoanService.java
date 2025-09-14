package com.zealepsoluciones.libertybackend.service;

import com.zealepsoluciones.libertybackend.model.entity.Loan;
import com.zealepsoluciones.libertybackend.model.enums.LoanStatus;

import java.util.List;

public interface LoanService {
    Loan createLoan(Loan loan);
    Loan getLoan(Long loanId);
    List<Loan> getAllLoans();
    Loan cancelLoan(Long loanId);
    Loan updateLoanStatus(Long loanId, LoanStatus status);
    void deleteLoan(Long loanId);
}
