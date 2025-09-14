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

    @Enumerated(EnumType.STRING)
    private PaymentType type; // NORMAL, EARLY, EXTRA, LATE_FEE

    @ManyToOne
    @JoinColumn(name = "installment_id")
    private Installment installment;
}

