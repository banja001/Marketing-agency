package com.example.marketingagencymanagement.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name="notifications")
public class Notification {
    @Id
    @SequenceGenerator(name = "notSeq", sequenceName = "notSeq", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notSeq")
    private Long id;

    @Column(name="user_id")
    private Long userId;

    @Column(name="message")
    private String message;

    @Column(name="date")
    private String date;
}
