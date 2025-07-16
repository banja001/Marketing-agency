package com.example.marketingagencymanagement.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static jakarta.persistence.InheritanceType.JOINED;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Inheritance(strategy=JOINED)
@Table(name = "permissions")
public class Permission {
    @Id
    @SequenceGenerator(name = "perSeq", sequenceName = "perSeq", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "perSeq")
    private Long id;

    @Column(name="name")
    private String name;

    @Column(name="description")
    private String description;


    //private List<Role> roles;

}
