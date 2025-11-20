package com.zealepsoluciones.libertybackend.repository;

import com.zealepsoluciones.libertybackend.model.entity.Transaction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;

public interface TransactionRepository extends CrudRepository<Transaction,Long> {
    @Query("SELECT t FROM Transaction t WHERE t.active = ?1 order by t.transactionDate desc")
    List<Transaction> findByActive(Boolean active);
    @Query("SELECT t FROM Transaction t WHERE t.transactionDate >= ?1 AND t.transactionDate <= ?2 and t.active = true order by t.transactionDate desc")
    List<Transaction> findByDateRange(LocalDate startDate, LocalDate endDate);
    @Query("SELECT t FROM Transaction t WHERE t.transactionDate >= ?1 AND t.transactionDate <= ?2 and t.type=?3 and t.active = true order by t.transactionDate desc")
    List<Transaction> findByDateRangeAndType(LocalDate startDate, LocalDate endDate, String type);
}
