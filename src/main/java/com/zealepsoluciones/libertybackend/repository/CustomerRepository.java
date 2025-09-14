package com.zealepsoluciones.libertybackend.repository;

import com.zealepsoluciones.libertybackend.model.entity.Customer;
import org.springframework.data.repository.CrudRepository;

public interface CustomerRepository extends CrudRepository<Customer,Long> {
}
