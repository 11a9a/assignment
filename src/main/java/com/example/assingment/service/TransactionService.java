package com.example.assingment.service;

import com.example.assingment.model.entity.Transaction;

import java.math.BigDecimal;

public interface TransactionService {
    Transaction topUp(Long customerId, BigDecimal amount);

    Transaction purchase(Long customerId, BigDecimal amount);

    Transaction refund(Long customerId, BigDecimal amount);
}
