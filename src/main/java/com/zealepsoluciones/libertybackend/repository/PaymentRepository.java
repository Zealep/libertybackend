package com.zealepsoluciones.libertybackend.repository;

import com.zealepsoluciones.libertybackend.model.entity.Payment;
import org.springframework.data.repository.CrudRepository;

public interface PaymentRepository extends CrudRepository<Payment,Long> {
}
