package com.project.demo.exceptions;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ValidationException extends RuntimeException {

    private List<String> errors= new ArrayList<>();

    public ValidationException(List<String> errors) {
        this.errors = errors;
    }

    public ValidationException() {
    }

    public ValidationException(String message) {
        super(message);
    }


}
