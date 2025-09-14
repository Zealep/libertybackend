package com.zealepsoluciones.libertybackend.repository;

import com.zealepsoluciones.libertybackend.model.entity.Loan;
import org.springframework.data.repository.CrudRepository;

public interface LoanRepository extends CrudRepository<Loan, Long> {
}
