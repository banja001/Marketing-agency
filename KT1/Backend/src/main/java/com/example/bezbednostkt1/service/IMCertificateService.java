package com.example.bezbednostkt1.service;

import com.example.bezbednostkt1.dto.CertificateDto;
import com.example.bezbednostkt1.dto.CreateCertificateDto;
import com.example.bezbednostkt1.dto.KeyPairDto;
import com.example.bezbednostkt1.model.Certificate;

import java.util.ArrayList;

public interface IMCertificateService {
    ArrayList<Certificate> getPossibleCertificates();
    KeyPairDto createCertificate(CreateCertificateDto certDto);
}
