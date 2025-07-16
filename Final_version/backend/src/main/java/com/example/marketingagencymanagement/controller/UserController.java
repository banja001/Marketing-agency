package com.example.marketingagencymanagement.controller;

import com.example.marketingagencymanagement.model.*;
import com.example.marketingagencymanagement.service.RoleService;
import com.example.marketingagencymanagement.dto.ClientDto;
import com.example.marketingagencymanagement.dto.*;
import com.example.marketingagencymanagement.exception.UsernameAlreadyExistsException;
import com.example.marketingagencymanagement.model.User;
import com.example.marketingagencymanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.text.ParseException;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;
    @GetMapping("/inactiveClients")
    public ResponseEntity<ArrayList<User>> getInactiveClients() {


            ArrayList<User> inactiveClients = userService.findInactiveClients();
            for (User client : inactiveClients) {
                client.setPassword("");
            }
            return new ResponseEntity<>(inactiveClients, HttpStatus.OK);

    }

    @GetMapping(value ="/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<UserProfileDto> getUserProfile(@PathVariable Long id) {
        User user = userService.findById(id);
        return new ResponseEntity<>(new UserProfileDto(user), HttpStatus.OK);
    }

    @PutMapping(value = "/edit", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<UserProfileDto> updateUser(@RequestBody UserProfileDto dto) {
        UserProfileDto updatedUser = null;
        try {
            User user = userService.updateUser(dto);
            updatedUser = new UserProfileDto(user);
            return new ResponseEntity<UserProfileDto>(updatedUser, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<UserProfileDto>(updatedUser, HttpStatus.CONFLICT);
        }
    }

    @PutMapping(value = "/changePassword", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Boolean> updatePassword(@RequestBody ChangePasswordDto password) {
        if(userService.changePassword(password))
            return new ResponseEntity<Boolean>(true, HttpStatus.OK);
        else
            return new ResponseEntity<Boolean>(false, HttpStatus.CONFLICT);
    }

    @GetMapping(value = "/role/{roleName}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<ArrayList<UserProfileDto>> getByRoleName(@PathVariable String roleName) {
        ArrayList<User> users = userService.findByRolesName(roleName);
        ArrayList<UserProfileDto> userDtos = new ArrayList<>();

        for (User user : users) {
            UserProfileDto userProfileDto = new UserProfileDto(user);
            userDtos.add(userProfileDto);
        }
        return new ResponseEntity<ArrayList<UserProfileDto>>(userDtos, HttpStatus.OK);
    }

    @PostMapping(value = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> createUser(@RequestBody RequestUser request) {
        UserCreationDto dto = request.getParam1();
        String jwtToken = request.getParam2();
        if (userService.hasAuthority(jwtToken,"SveVezanoZaKlijente")) {
            User user = null;
            try {
                user = userService.createUser(dto);
                return new ResponseEntity<User>(user, HttpStatus.CREATED);
            } catch (Exception e) {
                e.printStackTrace();
                return new ResponseEntity<User>(user, HttpStatus.CONFLICT);
            }
        }
        else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
    }

    @PostMapping(value = "/request/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Request> createRequest(@RequestBody RequestCreationDto dto) {
        Request request = null;
        try {
            request = userService.createRequest(dto);
            return new ResponseEntity<Request>(request, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Request>(request, HttpStatus.CONFLICT);
        }
    }

    @GetMapping(value ="/requests", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<ArrayList<Request>> getRequests() throws ParseException {
        ArrayList<Request> requests = userService.findAllRequestsNotInPast();
        return new ResponseEntity<>(requests, HttpStatus.OK);
    }

    @PostMapping(value = "/commercial/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Commercial> createRequest(@RequestBody CommercialDto dto) {
        Commercial commercial = null;
        try {
            commercial = userService.createCommercial(dto);
            return new ResponseEntity<Commercial>(commercial, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Commercial>(commercial, HttpStatus.CONFLICT);
        }
    }

    @DeleteMapping(value = "/requests/delete/{requestId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Void> deleteById(@PathVariable String requestId) {
        try {
            userService.deleteRequest(Long.parseLong(requestId));
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok().build();
        }
    }

    @GetMapping(value ="/commercials", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<ArrayList<CommercialDto>> getCommercials() {
        ArrayList<CommercialDto> commercialDtos = new ArrayList<>();
        ArrayList<Commercial> commercials = userService.findAllCommercials();
        for (Commercial commercial : commercials) {
            CommercialDto dto = new CommercialDto(commercial);
            commercialDtos.add(dto);
        }
        return new ResponseEntity<>(commercialDtos, HttpStatus.OK);
    }

    @GetMapping(value ="/employee/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<EmployeeDto> getEmployee(@PathVariable Long id) {
        Employee employee = userService.findEmployeeById(id);
        return new ResponseEntity<>(new EmployeeDto(employee), HttpStatus.OK);
    }

    @GetMapping(value = "sendMail/{username}", produces = "application/json")
    public ResponseEntity<Boolean> sendMailToActivate(@PathVariable String username) {
        try {
            boolean result = userService.sendMailToActivate(username);
            return ResponseEntity.ok(result);
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(false);
        }
    }

    @PostMapping(value = "blockRequest/{username}", produces = "application/json")
    public ResponseEntity<Boolean> blockRequest(@PathVariable String username, @RequestBody String reason){
        try {
            boolean result = userService.blockUser(username,reason);
            return ResponseEntity.ok(result);
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(false);
        }
    }

    @PostMapping(value = "verifyTotp", produces = "application/json")
    public ResponseEntity<TokenDto> verifyTotp(@RequestBody VerifyTotpDto verifyTotpDto){
        TokenDto result = new TokenDto();
        try {
            result = userService.verifyTotp(verifyTotpDto.getUsername(), verifyTotpDto.getCode());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return new ResponseEntity<TokenDto>(result, HttpStatus.CONFLICT);
        }
    }

    @PostMapping(value = "blockUser/{username}", produces = "application/json")
    public ResponseEntity<Boolean> blockUser(@PathVariable String username){
        try {
            boolean result = userService.blockUser(username);
            return ResponseEntity.ok(result);
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(false);
        }
    }

    @DeleteMapping(value = "/deleteAccount/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<Void> deleteAccount(@PathVariable String username) {
        if(userService.deleteAccount(username)){
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
