package com.zealepsoluciones.libertybackend.model.entity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zealepsoluciones.libertybackend.model.enums.InstallmentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
@Getter
@Setter
@Entity
public class Installment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer number; // Installment number (1,2,3…)
    private LocalDate dueDate;
    private BigDecimal amount;       // Total installment amount
    private BigDecimal interest;     // Interest portion
    private BigDecimal principalPart;// Principal portion

    private BigDecimal remainingBalance;

    @Enumerated(EnumType.STRING)
    private InstallmentStatus status; // PENDING, PAID, LATE

    @ManyToOne
    @JoinColumn(name = "loan_id")
    @JsonBackReference
    private Loan loan;

    @OneToMany(mappedBy = "installment", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Payment> payments;

}

