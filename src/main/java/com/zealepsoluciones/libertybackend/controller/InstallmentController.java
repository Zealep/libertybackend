package com.zealepsoluciones.libertybackend.controller;

import com.zealepsoluciones.libertybackend.model.entity.Installment;
import com.zealepsoluciones.libertybackend.model.entity.Loan;
import com.zealepsoluciones.libertybackend.service.InstallmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/installments")
@RequiredArgsConstructor
public class InstallmentController {
    private final InstallmentService installmentService;

    @PostMapping("/simulate")
    public ResponseEntity<List<Installment>> getAllInstallments(@RequestBody Loan loan) {
        return new ResponseEntity<>(installmentService.generateInstallments(loan), HttpStatus.OK);
    }
}
