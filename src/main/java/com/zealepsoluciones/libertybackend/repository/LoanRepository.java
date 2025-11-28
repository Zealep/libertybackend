package com.zealepsoluciones.libertybackend.repository;

import com.zealepsoluciones.libertybackend.model.entity.Loan;
import com.zealepsoluciones.libertybackend.model.enums.LoanStatus;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LoanRepository extends CrudRepository<Loan, Long> {

    List<Loan> findByStatus(LoanStatus status);
    List<Loan> findByStatusAndIsShortTerm(LoanStatus status, boolean term);
    List<Loan> findByIsShortTerm(boolean term);
}
