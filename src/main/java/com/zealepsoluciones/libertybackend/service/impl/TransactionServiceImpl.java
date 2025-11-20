package com.zealepsoluciones.libertybackend.service.impl;

import com.zealepsoluciones.libertybackend.model.entity.Transaction;
import com.zealepsoluciones.libertybackend.repository.TransactionRepository;
import com.zealepsoluciones.libertybackend.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    @Override
    public Transaction save(Transaction transaction) {
        transaction.setActive(true);
        return transactionRepository.save(transaction);
    }

    @Override
    public Transaction findById(Long id) {
        return transactionRepository.findById(id).orElse(null);
    }

    @Override
    public List<Transaction> findAll() {
        return (List<Transaction>)transactionRepository.findByActive(true);
    }

    @Override
    public void delete(Long id) {
        Transaction transaction = transactionRepository.findById(id).orElse(null);
        if(transaction != null){
            transaction.setActive(false);
            transactionRepository.save(transaction);
        }
    }

    @Override
    public List<Transaction> findByDateRange(String startDate, String endDate) {
        return transactionRepository.findByDateRange(LocalDate.parse(startDate),LocalDate.parse(endDate));
    }

    @Override
    public List<Transaction> findByDateRangeAndType(String startDate, String endDate, String type) {
        return transactionRepository.findByDateRangeAndType(LocalDate.parse(startDate),LocalDate.parse(endDate),type);
    }
}
