package com.zealepsoluciones.libertybackend.controller;

import com.zealepsoluciones.libertybackend.model.entity.Category;
import com.zealepsoluciones.libertybackend.model.entity.Transaction;
import com.zealepsoluciones.libertybackend.service.CategoryService;
import com.zealepsoluciones.libertybackend.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<Transaction> create(@RequestBody Transaction t) {
        return ResponseEntity.ok(transactionService.save(t));
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> list() {
        return ResponseEntity.ok(transactionService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transaction> findById(@PathVariable Long id) {
        return ResponseEntity.ok(transactionService.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        transactionService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/range")
    public ResponseEntity<List<Transaction>> findByDateRange(
            @RequestParam("start") String startDate,
            @RequestParam("end") String endDate,
            @RequestParam(value = "type",required = false) String type){

        if(type == null || type.isEmpty()){
            List<Transaction> transactions = transactionService.findByDateRange(startDate, endDate);
            return ResponseEntity.ok(transactions);
        }
        List<Transaction> transactions = transactionService.findByDateRangeAndType(startDate, endDate, type);
        return ResponseEntity.ok(transactions);
    }

}
