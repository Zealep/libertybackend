package com.zealepsoluciones.libertybackend.service;

import com.zealepsoluciones.libertybackend.model.entity.Transaction;

import java.util.List;

public interface TransactionService {
    Transaction save(Transaction transaction);
    Transaction findById(Long id);
    List<Transaction> findAll();
    void delete(Long id);
    List<Transaction> findByDateRange(String startDate, String endDate);
}
