package com.example.marketingagencymanagement.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;



@RestController
@RequestMapping("/api/commercials")
public class CommercialController {


    @GetMapping("/click")
    public ResponseEntity<Void> clickAd(@RequestParam String servicePackage) throws NoSuchAlgorithmException {
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
