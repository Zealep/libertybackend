package com.zealepsoluciones.libertybackend.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.zealepsoluciones.libertybackend.model.enums.State;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String documentNumber;
    private String email;
    private String phone;
    @Enumerated(EnumType.STRING)
    private State state;

    @OneToMany(mappedBy = "customer")
    @JsonManagedReference
    @JsonIgnore
    private List<Loan> loans;
}
