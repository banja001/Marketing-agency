package com.example.marketingagencymanagement.repository;

import com.example.marketingagencymanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.Optional;

public interface UserRepository  extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    ArrayList<User> findByRolesNameAndEnabledIsFalse(String roleName);
    Optional<User> findById(Long id);
    ArrayList<User> findAll();
    ArrayList<User> findByRolesName(String roleName);
    ArrayList<User> findByRolesNameAndEnabledIsFalseAndActivationPendingIsFalse(String roleName);
    void deleteById(Long id);

}
