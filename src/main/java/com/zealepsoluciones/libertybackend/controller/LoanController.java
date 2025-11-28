package com.zealepsoluciones.libertybackend.controller;

import com.zealepsoluciones.libertybackend.model.dto.LoanDTO;
import com.zealepsoluciones.libertybackend.model.entity.Loan;
import com.zealepsoluciones.libertybackend.model.enums.LoanStatus;
import com.zealepsoluciones.libertybackend.service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController {
    private final LoanService loanService;

    // Crear un préstamo
    @PostMapping
    public ResponseEntity<Loan> createLoan(@RequestBody Loan loan) {
        Loan savedLoan = loanService.createLoan(loan);
        return ResponseEntity.ok(loan);
    }

    // Obtener préstamo por ID
    @GetMapping("/{id}")
    public ResponseEntity<Loan> getLoanById(@PathVariable Long id) {
        return ResponseEntity.ok(loanService.getById(id));
    }

    // Listar todos los préstamos
    @GetMapping
    public ResponseEntity<List<LoanDTO>> getAllLoans() {
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

    @GetMapping("/filters")
    public ResponseEntity<List<LoanDTO>> getByFilters(
            @RequestParam(required = false) LoanStatus status,
            @RequestParam(required = false) String term) {

        List<LoanDTO> loans;

        if (status != null && term != null && !status.toString().isEmpty() && !term.isEmpty()) {
            // Filtrar por ambos
            boolean isShortTerm = "short".equalsIgnoreCase(term);
            loans = loanService.findByStatusAndTerm(status, isShortTerm);
        } else if (status != null && !status.toString().isEmpty()) {
            // Solo filtrar por estado
            loans = loanService.findByStatus(status);
        } else if (term != null && !term.isEmpty()) {
            // Solo filtrar por plazo
            boolean isShortTerm = "short".equalsIgnoreCase(term);
            loans = loanService.findByTerm(isShortTerm);
        } else {
            // Sin filtros, retornar todos
            loans = loanService.getAllLoans();
        }

        return ResponseEntity.ok(loans);
    }

}
