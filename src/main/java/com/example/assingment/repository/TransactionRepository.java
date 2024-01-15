package com.example.assingment.repository;

import com.example.assingment.model.entity.Customer;
import com.example.assingment.model.entity.Transaction;
import com.example.assingment.model.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Optional<Transaction> findTopByCustomerAndTypeOrderByIdDesc(Customer customer, TransactionType type);
}
