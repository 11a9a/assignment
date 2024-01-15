package com.example.assingment.controller;

import com.example.assingment.model.entity.Transaction;
import com.example.assingment.service.TransactionService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("api/v1/transactions")
@RequiredArgsConstructor
@SecurityRequirement(name = "basicAuth")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/{customerId}/top-up")
    public ResponseEntity<Transaction> topUp(@PathVariable Long customerId, @Positive(message = "Amount must be positive") @RequestParam BigDecimal amount) {
        Transaction transaction = transactionService.topUp(customerId, amount);
        return ResponseEntity.ok(transaction);
    }

    @PostMapping("/{customerId}/purchase")
    public ResponseEntity<Transaction> purchase(@PathVariable Long customerId, @Positive(message = "Amount must be positive") @RequestParam BigDecimal amount) {
        Transaction transaction = transactionService.purchase(customerId, amount);
        return ResponseEntity.ok(transaction);
    }

    @PostMapping("/{customerId}/refund")
    public ResponseEntity<Transaction> refund(@PathVariable Long customerId, @Positive(message = "Amount must be positive") @RequestParam BigDecimal amount) {
        Transaction transaction = transactionService.refund(customerId, amount);
        return ResponseEntity.ok(transaction);
    }
}
