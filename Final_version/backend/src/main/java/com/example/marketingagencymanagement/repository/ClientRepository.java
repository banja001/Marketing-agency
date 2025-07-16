package com.example.marketingagencymanagement.repository;

import com.example.marketingagencymanagement.model.Client;
import com.example.marketingagencymanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ClientRepository  extends JpaRepository<Client, Long> {
    Optional<Client> findByUsername(String username);
    Optional<Client> findById(Long id);
}
