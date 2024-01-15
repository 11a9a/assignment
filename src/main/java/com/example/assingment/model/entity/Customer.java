package com.example.assingment.model.entity;

import com.example.assingment.model.dto.CustomerDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String surname;
    private LocalDate birthDate;
    private String gsmNumber;
    private BigDecimal balance;

    public Customer() {
        // Set default balance to 100 AZN during customer creation
        this.balance = BigDecimal.valueOf(100.0);
    }

    public Customer(CustomerDto customerDto) {
        this.name = customerDto.name();
        this.surname = customerDto.surname();
        this.birthDate = customerDto.birthDate();
        this.gsmNumber = customerDto.gsmNumber();
        // Set default balance to 100 AZN during customer creation
        this.balance = BigDecimal.valueOf(100.0);
    }
}
