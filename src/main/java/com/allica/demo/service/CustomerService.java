package com.allica.demo.service;

import com.allica.demo.entity.CustomerEntity;
import com.allica.demo.model.request.CustomerDto;

import java.util.List;

public interface CustomerService {

     CustomerEntity save(CustomerDto customer);
     CustomerEntity update(CustomerDto customer, Long id);
     List<CustomerDto> findAll();
     CustomerDto findById(long id);

}
