package com.example.marketingagencymanagement.repository;

import com.example.marketingagencymanagement.model.Commercial;
import com.example.marketingagencymanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface CommercialRepository extends JpaRepository<Commercial, Long> {
    ArrayList<Commercial> findAll();
}
