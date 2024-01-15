package com.example.assingment.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

public record CustomerDto(@NotBlank(message = "Name must not be blank")
                          @NotNull(message = "Name must not be null")
                          String name,
                          @NotBlank(message = "Surname must not be blank")
                          @NotNull(message = "Surname must not be null")
                          String surname,
                          @NotNull(message = "Birth Date must not be null")
                          @Past(message = "Birth Date must be in the past")
                          LocalDate birthDate,
                          @NotBlank(message = "GSM Number must not be blank")
                          @NotNull(message = "GSM Number must not be null")
                          String gsmNumber) {
}
