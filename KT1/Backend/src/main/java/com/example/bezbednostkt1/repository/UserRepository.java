package com.example.bezbednostkt1.repository;

import com.example.bezbednostkt1.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository  extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}

