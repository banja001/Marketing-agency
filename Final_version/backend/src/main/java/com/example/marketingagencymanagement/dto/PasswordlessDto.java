package com.example.marketingagencymanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PasswordlessDto {

    private String email;
    private String recaptchaToken;

}

