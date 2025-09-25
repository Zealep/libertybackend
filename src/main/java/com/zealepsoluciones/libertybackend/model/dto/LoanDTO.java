package com.zealepsoluciones.libertybackend.model.dto;


import com.zealepsoluciones.libertybackend.model.enums.InterestType;
import com.zealepsoluciones.libertybackend.model.enums.LoanStatus;
import java.math.BigDecimal;
import java.time.LocalDate;

public record LoanDTO(
    Long id,
    BigDecimal principal,
    BigDecimal monthlyInterestRate,
    Integer termMonths,
    InterestType interestType,
    boolean isShortTerm,
    LoanStatus status,
    LocalDate disbursementDate,
    LocalDate shortTermEndDate,
    CustomerDTO customer
){
}
