package com.example.marketingagencymanagement.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import static jakarta.persistence.InheritanceType.JOINED;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "commercials")
public class Commercial implements Serializable {

        @Id
        @SequenceGenerator(name = "commSeq", sequenceName = "commSeq", initialValue = 1, allocationSize = 1)
        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "commSeq")
        @Column(name = "id")
        private Long id;

        @ManyToOne(cascade = CascadeType.ALL)
        @JoinColumn(name = "client_id")
        private Client client;

        @ManyToOne(cascade = CascadeType.MERGE)
        @JoinColumn(name = "employee_id")
        private Employee employee;

        @Column(name = "moto")
        private String moto;

        @Column(name = "duration")
        private String duration;

        @Column(name = "description")
        private String description;

}
