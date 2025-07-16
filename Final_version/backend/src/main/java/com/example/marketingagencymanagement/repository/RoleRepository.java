package com.example.marketingagencymanagement.repository;

import com.example.marketingagencymanagement.model.Client;
import com.example.marketingagencymanagement.model.Role;
import com.example.marketingagencymanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository  extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}
