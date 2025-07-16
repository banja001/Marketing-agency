package com.example.marketingagencymanagement.repository;

import com.example.marketingagencymanagement.dto.RequestCreationDto;
import com.example.marketingagencymanagement.model.Client;
import com.example.marketingagencymanagement.model.Request;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface RequestRepository extends JpaRepository<Request, Long> {
    ArrayList<Request> findAll();
    void deleteById(Long id);
}
