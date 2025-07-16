package com.example.marketingagencymanagement.controller;

import com.example.marketingagencymanagement.model.Role;
import com.example.marketingagencymanagement.model.User;
import com.example.marketingagencymanagement.service.RoleService;
import com.example.marketingagencymanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleService roleService;
    private final UserService userService;

    @Autowired
    public RoleController(RoleService roleService,UserService userService) {
        this.roleService = roleService;
        this.userService=userService;
    }

    @GetMapping
    public ResponseEntity<List<Role>> getAllRoles(@RequestBody String jwtToken) {
            if (userService.hasAuthority(jwtToken,"SveVezanoZaRole")) {
                List<Role> roles = roleService.getAllRoles();
                return new ResponseEntity<>(roles, HttpStatus.OK);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Role> getRoleById(@RequestBody String jwtToken, @PathVariable Long id) {
            if (userService.hasAuthority(jwtToken,"SveVezanoZaRole")) {
                Optional<Role> role = roleService.getRoleById(id);
                return role.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                        .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }

    }

    @PostMapping
    public ResponseEntity<Role> createRole(@RequestBody String jwtToken, @RequestBody Role role) {
            if (userService.hasAuthority(jwtToken,"SveVezanoZaRole")) {
                Role savedRole = roleService.saveRole(role);
                return new ResponseEntity<>(savedRole, HttpStatus.CREATED);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@RequestBody String jwtToken, @PathVariable Long id) {
            if (userService.hasAuthority(jwtToken,"SveVezanoZaRole")) {
                roleService.deleteRole(id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }
    }

    @DeleteMapping("/{id}/{permissionName}")
    public ResponseEntity<Void> deletePermissionForRole(@RequestBody String jwtToken, @PathVariable Long id,@PathVariable String permissionName) {
            if (userService.hasAuthority(jwtToken,"SveVezanoZaRole")) {
                roleService.deletePermissionForRole(id,permissionName);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }

    }
}
