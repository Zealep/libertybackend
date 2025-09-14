package com.zealepsoluciones.libertybackend.controller;

import com.zealepsoluciones.libertybackend.model.entity.Loan;
import com.zealepsoluciones.libertybackend.model.enums.LoanStatus;
import com.zealepsoluciones.libertybackend.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/loans")
@RequiredArgsConstructor
public class LoanController {
    private final LoanService loanService;

    // Crear un préstamo
    @PostMapping
    public ResponseEntity<Loan> createLoan(@RequestBody Loan loan) {
        Loan savedLoan = loanService.createLoan(loan);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedLoan);
    }

    // Obtener préstamo por ID
    @GetMapping("/{id}")
    public ResponseEntity<Loan> getLoanById(@PathVariable Long id) {
        return ResponseEntity.ok(loanService.getById(id));
    }

    // Listar todos los préstamos
    @GetMapping
    public ResponseEntity<List<Loan>> getAllLoans() {
        return ResponseEntity.ok(loanService.getAllLoans());
    }

    // Actualizar préstamo
    @PutMapping("/{id}")
    public ResponseEntity<Loan> updateLoan(
            @RequestBody Loan loanDetails) {
        return ResponseEntity.ok(loanService.updateLoan( loanDetails));
    }

    // Eliminar préstamo
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLoan(@PathVariable Long id) {
        loanService.deleteLoan(id);
        return ResponseEntity.noContent().build();
    }

    // Cambiar estado del préstamo (ej: CANCELLED, CLOSED)
    @PatchMapping("/{id}/status")
    public ResponseEntity<Loan> updateStatus(
            @PathVariable Long id,
            @RequestParam LoanStatus status) {
        return ResponseEntity.ok(loanService.updateLoanStatus(id, status));
    }
}
