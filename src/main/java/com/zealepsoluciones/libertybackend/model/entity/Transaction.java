package com.zealepsoluciones.libertybackend.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double amount;
    private String description;
    private String type;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    private LocalDate transactionDate;
    private String paymentMethod;
    private String reference;
    private String notes;
    private Boolean isRecurring;
    private String recurringFrequency;
    private String attachmentUrl;
    private Boolean active;

}
