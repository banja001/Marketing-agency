package com.example.marketingagencymanagement.dto;

import com.example.marketingagencymanagement.model.ClientType;
import com.example.marketingagencymanagement.model.Employee;
import com.example.marketingagencymanagement.model.ServicePackageType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto {
    private Long id;
    private String username;
    private String password;
    private String name;
    private String surname;
    private String address;
    private String city;
    private String country;
    private String phone;
    private Boolean enabled;
    private Boolean hasChangedPassword;

    public EmployeeDto(Employee employee) {
        this.id = employee.getId();
        this.username = employee.getUsername();
        this.password = null;
        this.name = employee.getName();
        this.surname = employee.getSurname();
        this.address = employee.getAddress();
        this.city = employee.getCity();
        this.country = employee.getCountry();
        this.phone = employee.getPhone();
        this.enabled = employee.isEnabled();
        this.hasChangedPassword = employee.getHasChangedPassword();
    }
}
