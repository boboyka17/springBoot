package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class BaseException extends ResponseStatusException {
    public BaseException(HttpStatus status, String message) {
        super(status,message);
    }
}
