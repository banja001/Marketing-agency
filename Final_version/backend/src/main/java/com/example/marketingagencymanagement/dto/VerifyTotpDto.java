package com.example.marketingagencymanagement.dto;

import lombok.Data;

@Data
public class VerifyTotpDto {
    private String username;
    private String code;
}
