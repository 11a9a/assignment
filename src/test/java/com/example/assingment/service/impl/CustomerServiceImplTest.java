package com.example.assingment.service.impl;

import com.example.assingment.model.entity.Customer;
import com.example.assingment.repository.CustomerRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getAllCustomersReturnsListOfCustomers() {
        when(customerRepository.findAll()).thenReturn(Arrays.asList(new Customer(), new Customer()));
        List<Customer> customers = customerService.getAllCustomers();
        assertEquals(2, customers.size());
        verify(customerRepository, times(1)).findAll();
    }

    @Test
    public void getCustomerByIdReturnsCustomer() {
        Long customerId = 1L;
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(new Customer()));
        Customer customer = customerService.getCustomerById(customerId);
        assertNotNull(customer);
        verify(customerRepository, times(1)).findById(customerId);
    }

    @Test
    public void getCustomerByIdThrowsExceptionWhenCustomerNotFound() {
        Long customerId = 1L;
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> customerService.getCustomerById(customerId));
        verify(customerRepository, times(1)).findById(customerId);
    }

    @Test
    public void createCustomerReturnsCreatedCustomer() {
        Customer customer = new Customer();
        when(customerRepository.save(customer)).thenReturn(customer);
        Customer createdCustomer = customerService.createCustomer(customer);
        assertEquals(customer, createdCustomer);
        verify(customerRepository, times(1)).save(customer);
    }
}