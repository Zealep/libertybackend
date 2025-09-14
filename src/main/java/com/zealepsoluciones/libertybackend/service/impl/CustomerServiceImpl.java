package com.zealepsoluciones.libertybackend.service.impl;

import com.zealepsoluciones.libertybackend.model.entity.Customer;
import com.zealepsoluciones.libertybackend.model.enums.State;
import com.zealepsoluciones.libertybackend.repository.CustomerRepository;
import com.zealepsoluciones.libertybackend.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    @Override
    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public Customer findById(Long id) {
        return customerRepository.findById(id).orElse(null);
    }

    @Override
    public List<Customer> findAll() {
        return customerRepository.findByState(State.ACTIVE);
    }

    @Override
    public void delete(Long id) {
        Customer customer = this.findById(id);
        if (customer != null) {
            customer.setState(State.INACTIVE);
            customerRepository.save(customer);
        }
    }
}
