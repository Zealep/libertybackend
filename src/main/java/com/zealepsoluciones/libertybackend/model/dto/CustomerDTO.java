package com.zealepsoluciones.libertybackend.model.dto;
// DTO (Data Transfer Object) for Customer entity


import com.zealepsoluciones.libertybackend.model.enums.State;

public record CustomerDTO(
    Long id,
    String firstName,
    String lastName,
    String documentNumber,
    String email,
    String phone,
    State state
) {}
