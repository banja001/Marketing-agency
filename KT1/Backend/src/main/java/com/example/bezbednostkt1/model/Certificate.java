package com.example.bezbednostkt1.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;
import java.security.cert.X509Certificate;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Certificate {
    @Id
    @SequenceGenerator(name = "certSeq", sequenceName = "certSeq", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "certSeq")
    @Column(columnDefinition = "BIGINT")
    private Long id;
    @ManyToOne
    private User subject;
    @ManyToOne
    private User issuer;
    private Date startDate;
    private Date endDate;
    private CertType type;
    @Column(length = 4096)
    private String publicKey;
    private BigInteger serialNumber;
    private boolean isValid;
}
