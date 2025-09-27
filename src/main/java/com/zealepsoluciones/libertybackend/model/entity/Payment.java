package com.zealepsoluciones.libertybackend.model.entity;
import com.zealepsoluciones.libertybackend.model.enums.PaymentType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate paymentDate;
    private BigDecimal amount;
    private String referenceNumber;
    private String paymentMethod; // e.g., CREDIT_CARD, BANK_TRANSFER
    private String notes;

    @ManyToOne
    @JoinColumn(name = "installment_id")
    private Installment installment;
}

