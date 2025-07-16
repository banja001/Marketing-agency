package com.example.marketingagencymanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserCreationDto {
    public String username;
    public String password;
    public String name;
    public String surname;
    public String address;
    public String city;
    public String country;
    public String phone;
    public String role;

}
