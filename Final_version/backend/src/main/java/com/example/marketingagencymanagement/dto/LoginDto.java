package com.example.marketingagencymanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {

    private String username;
    private String password;
    private String recaptchaToken;

}
