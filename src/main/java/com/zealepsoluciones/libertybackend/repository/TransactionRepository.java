package com.zealepsoluciones.libertybackend.repository;

import com.zealepsoluciones.libertybackend.model.entity.Transaction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;

public interface TransactionRepository extends CrudRepository<Transaction,Long> {
    List<Transaction> findByActive(Boolean active);
    @Query("SELECT t FROM Transaction t WHERE t.transactionDate >= ?1 AND t.transactionDate <= ?2 order by t.transactionDate desc")
    List<Transaction> findByDateRange(LocalDate startDate, LocalDate endDate);
}
