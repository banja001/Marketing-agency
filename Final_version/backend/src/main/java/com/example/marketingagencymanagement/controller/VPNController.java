package com.example.marketingagencymanagement.controller;

import com.example.marketingagencymanagement.dto.VpnMessageDto;
import com.example.marketingagencymanagement.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/vpn")
public class VPNController {
    @Autowired
    private RestTemplate restTemplate;
    @GetMapping
    public VpnMessageDto get() {
        String url = "http://10.13.13.1:3000/";
        String response = restTemplate.getForObject(url, String.class);
        VpnMessageDto message = new VpnMessageDto(response);
        return message;
    }
}
