package com.example.bezbednostkt1.dto;

import com.example.bezbednostkt1.model.Role;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String name;
    private String lastname;
    private String email;
    private String username;
    private String phoneNumber;
    private Role role;
    private String organization;
    private String country;
}
