package com.example.marketingagencymanagement.controller;

import com.example.marketingagencymanagement.model.Notification;
import com.example.marketingagencymanagement.model.Permission;
import com.example.marketingagencymanagement.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;
    @GetMapping("/{id}")
    public ResponseEntity<List<Notification>> getAllNotificationsForUser(@PathVariable String id) {
        int i=Integer.parseInt(id);
        return ResponseEntity.ok(notificationService.getAllNotificationByUserId(i));
    }
}
