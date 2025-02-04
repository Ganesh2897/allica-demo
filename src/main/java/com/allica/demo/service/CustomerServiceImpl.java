package com.allica.demo.service;

import com.allica.demo.entity.CustomerEntity;
import com.allica.demo.exception.CustomerNotFoundException;
import com.allica.demo.model.request.CustomerDto;
import com.allica.demo.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository){
        this.customerRepository=customerRepository;
    }

    @Override
    @Transactional
    public CustomerEntity save(CustomerDto customer) {
        CustomerEntity customerEntity =
                CustomerEntity.builder()
                        .firstName(customer.getFirstName())
                        .lastName(customer.getLastName())
                        .dateOfBirth(customer.getDateOfBirth())
                        .build();
        return customerRepository.save(customerEntity);
    }

    @Override
    @Transactional
    public CustomerEntity update(CustomerDto customer, Long id) {
        CustomerEntity existingCustomer=customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException("Customer does not exist with id : "+id));
        existingCustomer.setFirstName(customer.getFirstName());
        existingCustomer.setLastName(customer.getLastName());
        existingCustomer.setDateOfBirth(customer.getDateOfBirth());
        return customerRepository.save(existingCustomer);
    }

    @Override
    public List<CustomerDto> findAll() {
        return customerRepository.findAll()
                .stream().map(CustomerDto::getCustomerDetailsDto).collect(Collectors.toList());
    }

    @Override
    public CustomerDto findById(long id) {
        return customerRepository.findById(id).map(CustomerDto::getCustomerDetailsDto).orElseThrow(()->new CustomerNotFoundException("Customer does not exist with id : "+id));
    }
}
