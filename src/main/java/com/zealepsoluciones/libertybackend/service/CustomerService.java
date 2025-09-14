package com.zealepsoluciones.libertybackend.service;

import com.zealepsoluciones.libertybackend.model.entity.Customer;

import java.util.List;

public interface CustomerService {
    Customer save(Customer customer);
    Customer findById(Long id);
    List<Customer> findAll();
    void delete(Long id);

}
