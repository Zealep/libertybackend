package com.zealepsoluciones.libertybackend.repository;

import com.zealepsoluciones.libertybackend.model.entity.Payment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PaymentRepository extends CrudRepository<Payment,Long> {
    @Query("SELECT p FROM Payment p WHERE p.installment.loan.id = :loanId")
    List<Payment> findPaymentsByLoanId(Long loanId);
}
