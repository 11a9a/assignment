package com.example.assingment.exception.handler;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
public class GenericResponse {
    private String message;
    private List<Object> errors = new ArrayList<>();

    public GenericResponse(String message, List<Object> errors) {
        this.message = message;
        this.errors = errors;
    }

    public GenericResponse(String message, String error) {
        this.message = message;
        this.errors = Collections.singletonList(error);
    }
}
