package com.zealepsoluciones.libertybackend.controller;

import com.zealepsoluciones.libertybackend.model.entity.Installment;
import com.zealepsoluciones.libertybackend.model.entity.Loan;
import com.zealepsoluciones.libertybackend.service.InstallmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/installments")
@RequiredArgsConstructor
public class InstallmentController {
    private final InstallmentService installmentService;

    @GetMapping("/simulate")
    public ResponseEntity<List<Installment>> getAllInstallments(@RequestBody Loan loan) {
        return new ResponseEntity<>(installmentService.generateInstallments(loan), HttpStatus.OK);
    }
}
