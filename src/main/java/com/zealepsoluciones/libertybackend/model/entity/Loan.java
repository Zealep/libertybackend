package com.zealepsoluciones.libertybackend.model.entity;
import com.zealepsoluciones.libertybackend.model.enums.InterestType;
import com.zealepsoluciones.libertybackend.model.enums.LoanStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    private BigDecimal principal;           // Loan amount
    private BigDecimal monthlyInterestRate; // Example: 0.02 = 2%
    private Integer termMonths;             // Loan term

    @Enumerated(EnumType.STRING)
    private InterestType interestType; // SIMPLE or FRENCH

    @Enumerated(EnumType.STRING)
    private LoanStatus status = LoanStatus.ACTIVE;

    private LocalDate disbursementDate;

    @OneToMany(mappedBy = "loan", cascade = CascadeType.ALL)
    private List<Installment> installments;
}

