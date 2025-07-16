package com.example.marketingagencymanagement.controller;


import com.example.marketingagencymanagement.dto.*;
import com.example.marketingagencymanagement.model.Role;
import com.example.marketingagencymanagement.exception.UsernameAlreadyExistsException;
import com.example.marketingagencymanagement.model.Client;
import com.example.marketingagencymanagement.model.ClientType;
import com.example.marketingagencymanagement.model.User;
import com.example.marketingagencymanagement.service.ClientService;
import com.example.marketingagencymanagement.service.NotificationService;
import com.example.marketingagencymanagement.service.UserService;
import dev.samstevens.totp.exceptions.QrGenerationException;
import jakarta.annotation.security.PermitAll;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.io.IOException;
import java.util.Optional;
import java.util.regex.Pattern;


@RestController
@RequestMapping(value = "/api/auth")
public class AuthenticationController {

    @Autowired
    private UserService userService;

    @Autowired
    private ClientService clientService;
    @Autowired
    private NotificationService notificationService;
    @PostMapping(value = "/login", consumes = "application/json")
    public ResponseEntity<TokenDto> login(@RequestBody LoginDto loginDto, HttpServletRequest request){

        return ResponseEntity.ok(userService.login(loginDto));
    }
    @PostMapping("/register")
    public ResponseEntity<?> registerClient(@RequestBody ClientDto clientDto) throws Exception {
        if (!isValid(clientDto)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        try {
            return ResponseEntity.ok(clientService.create(clientDto));
        } catch (UsernameAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        } catch (QrGenerationException e) {
            return ResponseEntity.internalServerError().body("Something went wrong. Try again.");
        }
    }


    @PutMapping(value = "sendVerificationEmail", produces = "application/json", consumes = "application/json")
    public ResponseEntity<Boolean> sendVerificationEmail(@RequestBody PasswordlessDto passwordlessDto){
        return ResponseEntity.ok(userService.sendVerificationEmail(passwordlessDto));
    }

    @GetMapping("passwordlessLogin/verify/{token}")
    public ResponseEntity<String>  verifyUser(@PathVariable String token) {
        return ResponseEntity.ok(userService.validateVerificationToken(token));
    }

    @GetMapping("passwordlessLogin/{username}")
    public ResponseEntity<TokenDto> passwordlessLogin(@PathVariable String username) {
        return ResponseEntity.ok(userService.passwordlessLogin(username));
    }

    @PostMapping("refreshToken")
    public ResponseEntity<TokenDto> refreshToken(@RequestBody String refreshToken) {
        System.out.println("USAO U REFRESH");
        return ResponseEntity.ok(userService.refreshToken(refreshToken));
    }

    @GetMapping("/activateAccount/{token}")
    public ResponseEntity<Boolean>  activateAccount(@PathVariable String token) {
        return ResponseEntity.ok(userService.validateActivationToken(token));
    }
    private boolean isValidEmailAddress(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return Pattern.compile(emailRegex).matcher(email).matches();
    }
    private boolean isValid(ClientDto clientDto) {
        return isValidEmail(clientDto.getUsername()) && isValidPassword(clientDto.getPassword())
                && isValidPhoneNumber(clientDto.getPhone()) && isValidAddress(clientDto.getAddress(),clientDto.getCity(),clientDto.getCountry())
                && isValidClientType(clientDto);
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return Pattern.compile(emailRegex).matcher(email).matches();
    }
    private boolean isValidPassword(String password) {
        if (password.length() < 8) {
            return false;
        }
        if (!password.matches(".*[A-Z].*")) {
            return false;
        }
        if (!password.matches(".*[a-z].*")) {
            return false;
        }
        if (!password.matches(".*[!@#$%^&*()'+,-./:;<=>?{|}_].*")) {
            return false;
        }
        if (!password.matches(".*\\d.*")) {
            return false;
        }
        return true;
    }
    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber.matches("^(\\+381|0)[6-9][0-9]{6,8}$");
    }
    private boolean isValidAddress(String address, String city, String country) {
        if (address == null || address.isEmpty()) {
            return false;
        }
        if (city == null || city.isEmpty()) {
            return false;
        }
        if (country == null || country.isEmpty()) {
            return false;
        }
        return true;
    }
    private boolean isValidClientType(ClientDto clientDto) {
        if (clientDto.getClientType() == ClientType.PHYSICAL_PERSON) {
            if (clientDto.getName() == null || clientDto.getName().isEmpty()) {
                return false;
            }
            if (clientDto.getSurname() == null || clientDto.getSurname().isEmpty()) {
                return false;
            }
            return true;
        }
        if(clientDto.getClientType() == ClientType.LEGAL_ENTITY){
            if (clientDto.getCompanyName() == null || clientDto.getCompanyName() .isEmpty()) {
                return false;
            }
            if (clientDto.getTin() == null || clientDto.getTin().isEmpty()) {
                return false;
            }
            return true;
        }
       return false;
    }

    @PutMapping(value = "sendForgottenPasswordMail/", produces = "application/json", consumes = "application/json")
    public ResponseEntity<Boolean> sendForgottenPasswordMail(@RequestBody PasswordlessDto passwordlessDto){
        return ResponseEntity.ok(userService.sendMailForForgottenPassword(passwordlessDto));
    }

    @PutMapping(value = "/recoverAccount/", produces = "application/json", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Boolean> recoverAccount(@RequestBody RecoverAccountDto credentials) {
        if(userService.recoverAccount(credentials))
            return new ResponseEntity<Boolean>(true, HttpStatus.OK);
        else
            return new ResponseEntity<Boolean>(false, HttpStatus.CONFLICT);
    }



}
