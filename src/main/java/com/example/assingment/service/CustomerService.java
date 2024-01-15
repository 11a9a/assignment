package com.example.assingment.service;

import com.example.assingment.model.entity.Customer;

import java.util.List;

public interface CustomerService {
    List<Customer> getAllCustomers();

    Customer getCustomerById(Long customerId);

    Customer createCustomer(Customer customer);
}
