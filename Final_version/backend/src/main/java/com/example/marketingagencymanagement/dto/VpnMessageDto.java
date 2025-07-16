package com.example.marketingagencymanagement.dto;

public class VpnMessageDto {

    private String message;

    // Default constructor
    public VpnMessageDto() {
    }

    // Constructor with message parameter
    public VpnMessageDto(String message) {
        this.message = message;
    }

    // Getter for message
    public String getMessage() {
        return message;
    }

    // Setter for message
    public void setMessage(String message) {
        this.message = message;
    }
}
