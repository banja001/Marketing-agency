package com.example.marketingagencymanagement.dto;

import com.example.marketingagencymanagement.model.Client;
import com.example.marketingagencymanagement.model.Commercial;

public class CommercialDto {
    private ClientDto client;
    private EmployeeDto employee;
    private String moto;
    private String duration;
    private String description;
    public CommercialDto() {
    }

    public EmployeeDto getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeDto employee) {
        this.employee = employee;
    }

    public ClientDto getClient() {
        return client;
    }

    public void setClient(ClientDto client) {
        this.client = client;
    }

    public String getMoto() {
        return moto;
    }

    public void setMoto(String moto) {
        this.moto = moto;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CommercialDto(Commercial commercial) {
        this.client = new ClientDto(commercial.getClient());
        this.employee = new EmployeeDto(commercial.getEmployee());
        this.moto = commercial.getMoto();
        this.duration = commercial.getDuration();
        this.description = commercial.getDescription();
    }
}
