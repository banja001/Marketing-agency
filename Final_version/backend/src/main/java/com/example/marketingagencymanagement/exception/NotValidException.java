package com.example.marketingagencymanagement.exception;

public class NotValidException extends RuntimeException {
    public NotValidException(){}
    public NotValidException(String message) {
        super(message);
    }
}
