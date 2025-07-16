package com.example.bezbednostkt1.service;

import com.example.bezbednostkt1.dto.*;
import com.example.bezbednostkt1.model.Certificate;
import java.lang.reflect.Array;
import java.util.Optional;

import java.security.KeyPair;
import java.util.ArrayList;




public interface CertificateService {
    KeyPairDto createRootCertificate(CreateCertificateDto certDto);
    Optional<Certificate> getRootCertificate(String id);
    ArrayList<Certificate> getAll();
    void checkExpirationDate(ArrayList<Certificate> certificates);
    ArrayList<Certificate> getPredecessorsChain(Long certificateId);
    ArrayList<Certificate> getSuccessorsChain(Long certificateId);

    CertificateDto revokeCertificate(String id);
    boolean search(String param);

}
