package com.example.assingment.service.impl;

import com.example.assingment.exception.InsufficientBalanceException;
import com.example.assingment.model.entity.Customer;
import com.example.assingment.model.entity.Transaction;
import com.example.assingment.model.enums.TransactionType;
import com.example.assingment.repository.CustomerRepository;
import com.example.assingment.repository.TransactionRepository;
import com.example.assingment.service.TransactionService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final CustomerRepository customerRepository;

    private final TransactionRepository transactionRepository;

    @Override
    public Transaction topUp(Long customerId, BigDecimal amount) {
        Customer customer = getCustomerById(customerId);
        BigDecimal newBalance = customer.getBalance().add(amount);
        updateCustomerBalance(customer, newBalance);
        return saveTransaction(customer, TransactionType.TOP_UP, amount);
    }

    @Override
    public Transaction purchase(Long customerId, BigDecimal amount) {
        Customer customer = getCustomerById(customerId);
        if (customer.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException("Insufficient balance for the transaction");
        }
        BigDecimal newBalance = customer.getBalance().subtract(amount);
        updateCustomerBalance(customer, newBalance);
        return saveTransaction(customer, TransactionType.PURCHASE, amount);
    }

    @Override
    public Transaction refund(Long customerId, BigDecimal amount) {
        Customer customer = getCustomerById(customerId);
        Transaction lastPurchaseTransaction = transactionRepository.findTopByCustomerAndTypeOrderByIdDesc(customer, TransactionType.PURCHASE)
                .orElseThrow(() -> new EntityNotFoundException("No purchase transactions found for the customer"));
        Transaction lastRefundTransaction = transactionRepository.findTopByCustomerAndTypeOrderByIdDesc(customer, TransactionType.REFUND)
                .orElse(null);

        if (amount.compareTo(lastPurchaseTransaction.getAmount()) > 0) {
            throw new IllegalArgumentException("Refund amount cannot be greater than the last purchase transaction amount");
        }
        if (lastRefundTransaction != null && lastRefundTransaction.getTimestamp().isAfter(lastPurchaseTransaction.getTimestamp())) {
            throw new IllegalArgumentException("Cannot refund again until another purchase has been made");
        }
        BigDecimal newBalance = customer.getBalance().add(amount);
        updateCustomerBalance(customer, newBalance);
        return saveTransaction(customer, TransactionType.REFUND, amount);
    }

    private Customer getCustomerById(Long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));
    }

    private void updateCustomerBalance(Customer customer, BigDecimal newBalance) {
        customer.setBalance(newBalance);
        customerRepository.save(customer);
    }

    private Transaction saveTransaction(Customer customer, TransactionType type, BigDecimal amount) {
        Transaction transaction = new Transaction();
        transaction.setCustomer(customer);
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setType(type);
        transaction.setAmount(amount);
        return transactionRepository.save(transaction);
    }
}
