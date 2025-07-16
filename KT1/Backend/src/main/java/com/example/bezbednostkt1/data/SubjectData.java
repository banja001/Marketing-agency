package com.example.bezbednostkt1.data;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bouncycastle.asn1.x500.X500Name;

import java.security.PublicKey;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubjectData {
    // podaci za subjecta
    private PublicKey publicKey;
    private X500Name x500name;
    // podaci za sertifikat
    private String serialNumber;
    private Date startDate;
    private Date endDate;
}
