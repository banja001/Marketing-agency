package com.example.marketingagencymanagement.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;

import static jakarta.persistence.InheritanceType.JOINED;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Inheritance(strategy=JOINED)
@Table(name = "requests")
public class Request implements Serializable {

    @Id
    @SequenceGenerator(name = "reqSeq", sequenceName = "reqSeq", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reqSeq")
    @Column(name = "id")
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "client_id")
    private Client client;

    @Column(name = "deadline")
    private String deadline;

    @Column(name = "active_from")
    private String activeFrom;

    @Column(name = "active_to")
    private String activeTo;

    @Column(name = "description")
    private String description;
}
