package com.zealepsoluciones.libertybackend.service;

import com.zealepsoluciones.libertybackend.model.entity.Installment;
import com.zealepsoluciones.libertybackend.model.entity.Loan;

import java.util.List;

public interface InstallmentService {
    List<Installment> generateInstallments(Loan loan);
}
