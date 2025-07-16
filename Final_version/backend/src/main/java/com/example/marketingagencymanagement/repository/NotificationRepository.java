package com.example.marketingagencymanagement.repository;

import com.example.marketingagencymanagement.model.Employee;
import com.example.marketingagencymanagement.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
