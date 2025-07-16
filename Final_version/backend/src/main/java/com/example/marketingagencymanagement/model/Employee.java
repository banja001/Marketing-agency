package com.example.marketingagencymanagement.model;

import com.example.marketingagencymanagement.dto.ClientDto;
import com.example.marketingagencymanagement.dto.EmployeeDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;

@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "employees")
@PrimaryKeyJoinColumn(name = "employee_id")
public class Employee extends User{
    @Column(name = "has_changed_password")
    private Boolean hasChangedPassword;

    public Employee(Long id, String name, String surname, String address, String city, String country, String phone, String username, String password, boolean enabled, boolean activationPending, LocalDateTime blocked, ArrayList<Role> roles, Boolean hasChangedPassword, boolean isBlockedUser) {
        super(id,
                name,
                surname,
                address,
                city,
                country,
                phone,
                username,
                password,
                enabled,
                activationPending,
                blocked,
                false,
                null,
                isBlockedUser,
                roles
                );
        this.hasChangedPassword = hasChangedPassword;
    }

    public Employee(EmployeeDto dto) {
        super(null,
                dto.getName(),
                dto.getSurname(),
                dto.getAddress(),
                dto.getCity(),
                dto.getCountry(),
                dto.getPhone(),
                dto.getUsername(),
                null,
                false,
                false,
                null,
                false,
                null,
                false,
                null);
        this.hasChangedPassword = dto.getHasChangedPassword();
    }
}
