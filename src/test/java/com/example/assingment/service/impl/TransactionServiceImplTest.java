package com.example.assingment.service.impl;

import com.example.assingment.exception.InsufficientBalanceException;
import com.example.assingment.model.entity.Customer;
import com.example.assingment.model.entity.Transaction;
import com.example.assingment.model.enums.TransactionType;
import com.example.assingment.repository.CustomerRepository;
import com.example.assingment.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

public class TransactionServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void purchaseShouldThrowExceptionWhenBalanceIsInsufficient() {
        Long customerId = 1L;
        BigDecimal amount = BigDecimal.valueOf(600);
        Customer customer = new Customer();
        customer.setId(customerId);
        customer.setBalance(BigDecimal.valueOf(500));

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));

        assertThrows(InsufficientBalanceException.class, () -> transactionService.purchase(customerId, amount));
    }

    @Test
    public void purchaseShouldSucceedWhenBalanceIsSufficient() {
        Long customerId = 1L;
        BigDecimal amount = BigDecimal.valueOf(100);
        Customer customer = new Customer();
        customer.setId(customerId);
        customer.setBalance(BigDecimal.valueOf(500));

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(i -> i.getArguments()[0]);

        Transaction result = transactionService.purchase(customerId, amount);

        assertEquals(TransactionType.PURCHASE, result.getType());
        assertEquals(amount, result.getAmount());
    }

    @Test
    public void topUpShouldIncreaseCustomerBalance() {
        Long customerId = 1L;
        BigDecimal amount = BigDecimal.valueOf(100);
        Customer customer = new Customer();
        customer.setId(customerId);
        customer.setBalance(BigDecimal.valueOf(500));

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(i -> i.getArguments()[0]);

        Transaction result = transactionService.topUp(customerId, amount);

        assertEquals(TransactionType.TOP_UP, result.getType());
        assertEquals(amount, result.getAmount());
    }

    @Test
    public void refundShouldThrowExceptionWhenLastRefundIsAfterLastPurchase() {
        Long customerId = 1L;
        BigDecimal amount = BigDecimal.valueOf(100);
        Customer customer = new Customer();
        customer.setId(customerId);
        customer.setBalance(BigDecimal.valueOf(500));

        Transaction lastPurchaseTransaction = new Transaction();
        lastPurchaseTransaction.setAmount(BigDecimal.valueOf(200));
        lastPurchaseTransaction.setTimestamp(LocalDateTime.now().minusDays(1)); // ensure the timestamp is not null

        Transaction lastRefundTransaction = new Transaction();
        lastRefundTransaction.setAmount(BigDecimal.valueOf(50));
        lastRefundTransaction.setTimestamp(LocalDateTime.now()); // ensure the timestamp is not null

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(transactionRepository.findTopByCustomerAndTypeOrderByIdDesc(customer, TransactionType.PURCHASE)).thenReturn(Optional.of(lastPurchaseTransaction));
        when(transactionRepository.findTopByCustomerAndTypeOrderByIdDesc(customer, TransactionType.REFUND)).thenReturn(Optional.of(lastRefundTransaction));

        assertThrows(IllegalArgumentException.class, () -> transactionService.refund(customerId, amount));
    }

    @Test
    public void refundShouldThrowExceptionWhenRefundAmountIsGreaterThanLastPurchase() {
        Long customerId = 1L;
        BigDecimal amount = BigDecimal.valueOf(300);
        Customer customer = new Customer();
        customer.setId(customerId);
        customer.setBalance(BigDecimal.valueOf(500));

        Transaction lastPurchaseTransaction = new Transaction();
        lastPurchaseTransaction.setAmount(BigDecimal.valueOf(200));

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(transactionRepository.findTopByCustomerAndTypeOrderByIdDesc(customer, TransactionType.PURCHASE)).thenReturn(Optional.of(lastPurchaseTransaction));

        assertThrows(IllegalArgumentException.class, () -> transactionService.refund(customerId, amount));
    }

    @Test
    public void refundShouldSucceedWhenConditionsAreMet() {
        Long customerId = 1L;
        BigDecimal amount = BigDecimal.valueOf(100);
        Customer customer = new Customer();
        customer.setId(customerId);
        customer.setBalance(BigDecimal.valueOf(500));

        Transaction lastPurchaseTransaction = new Transaction();
        lastPurchaseTransaction.setAmount(BigDecimal.valueOf(200));

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(transactionRepository.findTopByCustomerAndTypeOrderByIdDesc(customer, TransactionType.PURCHASE)).thenReturn(Optional.of(lastPurchaseTransaction));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(i -> i.getArguments()[0]);

        Transaction result = transactionService.refund(customerId, amount);

        assertEquals(TransactionType.REFUND, result.getType());
        assertEquals(amount, result.getAmount());
    }
}