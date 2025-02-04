package com.allica.demo.service;
import com.allica.demo.entity.CustomerEntity;
import com.allica.demo.exception.CustomerNotFoundException;
import com.allica.demo.model.request.CustomerDto;
import com.allica.demo.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private CustomerDto customerDto;
    private CustomerEntity customerEntity;

    @BeforeEach
    void setUp() {
        customerDto=CustomerDto.builder()
                .firstName("Ross")
                .lastName("Geller")
                .dateOfBirth(LocalDate.of(1970,12,12))
                .build();
        customerEntity= CustomerEntity.builder()
                .id(1L)
                .firstName("Ross")
                .lastName("Geller")
                .dateOfBirth(LocalDate.of(1970,12,12))
                .build();
    }

    @Test
    void save_success() {
        when(customerRepository.save(any(CustomerEntity.class))).thenReturn(customerEntity);
        CustomerEntity customer = customerService.save(customerDto);
        assertNotNull(customer);
        assertEquals("Ross", customer.getFirstName());
        verify(customerRepository).save(any(CustomerEntity.class));
    }

    @Test
    void update_success() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customerEntity));
        when(customerRepository.save(any(CustomerEntity.class))).thenReturn(customerEntity);
        CustomerEntity customer = customerService.update(customerDto, 1L);
        assertNotNull(customer);
        assertEquals("Ross", customer.getFirstName());
        verify(customerRepository).save(customerEntity);
    }

    @Test
    void update_customerNotFound() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());
        Exception exception = assertThrows(CustomerNotFoundException.class, () -> {
            customerService.update(customerDto, 1L);
        });

        assertEquals("Customer does not exist with id : 1", exception.getMessage());
    }

    @Test
    void findAll_success() {
        when(customerRepository.findAll()).thenReturn(List.of(customerEntity));
        List<CustomerDto> customers = customerService.findAll();
        assertEquals(1, customers.size());
        verify(customerRepository).findAll();
    }

    @Test
    void findById_success() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customerEntity));
        CustomerDto customer = customerService.findById(1L);
        assertNotNull(customer);
        assertEquals("Ross", customer.getFirstName());
    }

    @Test
    void findById_ShouldThrowExceptionIfNotFound() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());
        Exception exception = assertThrows(CustomerNotFoundException.class, () -> {
            customerService.findById(1L);
        });
        assertEquals("Customer does not exist with id : 1", exception.getMessage());
    }
}
