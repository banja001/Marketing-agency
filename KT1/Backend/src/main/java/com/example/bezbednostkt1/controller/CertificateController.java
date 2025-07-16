package com.example.bezbednostkt1.controller;

import com.example.bezbednostkt1.dto.*;
import com.example.bezbednostkt1.model.Certificate;
import com.example.bezbednostkt1.service.CertificateService;
import com.example.bezbednostkt1.service.IMCertificateService;
import com.example.bezbednostkt1.service.CertificateServiceImpl;
import com.example.bezbednostkt1.service.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;


@RestController
@RequestMapping(value = "/api/cert")
public class CertificateController {
    @Autowired
    private CertificateService certificateService;

    @Autowired
    private IMCertificateService imCertificateService;

    @PostMapping(value = "/createRootCert", consumes = "application/json")
    public ResponseEntity<KeyPairDto> createRootCertificate(@RequestBody CreateCertificateDto certDto){
        return ResponseEntity.ok(certificateService.createRootCertificate(certDto));
    }

    @GetMapping(value = "/getRoot/{id}")
    public ResponseEntity<Optional<Certificate>> getRootCertificate(@PathVariable String id){
        return ResponseEntity.ok(certificateService.getRootCertificate(id));
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(value = "/getCertificates")
    public ResponseEntity<ArrayList<Certificate>> getAllCertificates() {
        ArrayList<Certificate> result = certificateService.getAll();
        certificateService.checkExpirationDate(result);
        return ResponseEntity.ok(certificateService.getAll());
    }

    @GetMapping(value = "/getImIssuers")
    public ResponseEntity<ArrayList<Certificate>> getPossibleImIssuerCertificates() {
        return ResponseEntity.ok(imCertificateService.getPossibleCertificates());
    }
    @PostMapping(value = "/createCert", consumes = "application/json")
    public ResponseEntity<KeyPairDto> createCertificate(@RequestBody CreateCertificateDto certDto){
        return ResponseEntity.ok(imCertificateService.createCertificate(certDto));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping(value = "/revokeCert/{id}", produces = "application/json", consumes = "application/json")
    public ResponseEntity<CertificateDto> revokeCertificate(@PathVariable String id,
                                                                   @RequestBody CertificateDto certDto) {
        return ResponseEntity.ok(certificateService.revokeCertificate(id));
    }

    @GetMapping(value = "/search/{param}")
    public ResponseEntity<Boolean> search(@PathVariable String param){
        return ResponseEntity.ok(certificateService.search(param));
    }
}
