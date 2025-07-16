package com.example.bezbednostkt1.dto;

import com.example.bezbednostkt1.model.CertType;
import com.example.bezbednostkt1.model.User;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CertificateDto {
    private Long id;
    private UserDto subject;
    private UserDto issuer;
    private Date startDate;
    private Date endDate;
    private String publicKey;
    private boolean isValid;

}
