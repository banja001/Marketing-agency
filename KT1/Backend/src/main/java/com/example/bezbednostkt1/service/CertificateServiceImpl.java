package com.example.bezbednostkt1.service;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.example.bezbednostkt1.data.IssuerData;
import com.example.bezbednostkt1.dto.*;
import com.example.bezbednostkt1.model.CertType;
import com.example.bezbednostkt1.model.Certificate;
import com.example.bezbednostkt1.data.SubjectData;
import com.example.bezbednostkt1.model.User;
import com.example.bezbednostkt1.pki.keystores.KeyStoreReader;
import com.example.bezbednostkt1.pki.keystores.KeyStoreWriter;
import com.example.bezbednostkt1.repository.CertificateRepository;
import com.example.bezbednostkt1.repository.UserRepository;
import com.fasterxml.jackson.core.base.ParserBase;
import jakarta.faces.convert.BigIntegerConverter;
import org.bouncycastle.util.test.FixedSecureRandom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.origin.SystemEnvironmentOrigin;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.spi.CharsetProvider;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.X509Certificate;
import java.util.*;



@Service
public class CertificateServiceImpl implements CertificateService {
    private CertificateGenerator generator = new CertificateGenerator();

    @Autowired
    private CertificateRepository certificateRepository;

    @Autowired
    private UserRepository userRepository;

    public KeyPairDto createRootCertificate(CreateCertificateDto certDto) {
        KeyPair keyPair = null;
        keyPair = generator.generateKeyPair();

        Optional<User> rootUser = userRepository.findByUsername(certDto.getSubjectUsername());
        if (rootUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "message: Could not find the root user!");
        }

        SubjectData subjectData = generator.generateSubjectData(rootUser.get(), keyPair.getPublic());
        //privatni kljuc
        IssuerData issuerData = generator.generateIssuerData(rootUser.get(), keyPair.getPrivate());

        X509Certificate cert = generator.generateCertificate(subjectData, issuerData);

        certificateRepository.save(createDatabaseRootCertificate(rootUser.get(), keyPair.getPublic(),cert.getSerialNumber()));
        KeyStoreWriter keyStoreWriter=new KeyStoreWriter();
        keyStoreWriter.loadKeyStore(null,(rootUser.get().getName()+rootUser.get().getId()).toCharArray());
        keyStoreWriter.write(cert.getSerialNumber().toString(),keyPair.getPrivate(),(rootUser.get().getName()+rootUser.get().getId()).toCharArray(),cert);
        keyStoreWriter.saveKeyStore("root_"+cert.getSerialNumber()+"_"+rootUser.get().getId()+".jks",(rootUser.get().getName()+rootUser.get().getId()).toCharArray());

        KeyStoreReader keyStoreReader=new KeyStoreReader();
        var a=keyStoreReader.readCertificate("root_"+cert.getSerialNumber()+"_"+rootUser.get().getId()+".jks",(rootUser.get().getName()+rootUser.get().getId()),cert.getSerialNumber().toString());
        System.out.println(a.toString());
        return new KeyPairDto(Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded()),
                Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()));
    }

    private Certificate createDatabaseRootCertificate(User rootUser, PublicKey pubKey, BigInteger serNum){
        Certificate cert = new Certificate();

        cert.setSubject(rootUser);
        cert.setIssuer(rootUser);
        cert.setPublicKey(Base64.getEncoder().encodeToString(pubKey.getEncoded()));
        cert.setSerialNumber(serNum);
        cert.setValid(true);
        Date today = new Date();
        Date endDate = generator.generateEndDate(today, CertType.ROOT);

        cert.setStartDate(today);
        cert.setEndDate(endDate);
        cert.setType(CertType.ROOT);
        cert.setValid(true);
        return cert;
    }

    @Override
    public Optional<Certificate> getRootCertificate(String subjectId) {

        Optional<Certificate> cert = certificateRepository.findBySubjectId(Long.valueOf(subjectId));
        if (cert.isEmpty()) {
            return null;
        }
        return cert;
    }

    @Override
    public ArrayList<Certificate> getAll() {
        return (ArrayList<Certificate>) certificateRepository.findAll();
    }

    @Override
    public void checkExpirationDate(ArrayList<Certificate> certificates){
        for(Certificate certificate : certificates){
            if(isExpired(certificate) || certificate.isValid() == false){ //if revoked before
                certificate.setValid(false);
            } else {
                certificate.setValid(true);
            }
            certificateRepository.saveAll(certificates);
        }
    }

    private boolean isExpired(Certificate certificate) {
        Date currentDate = new Date();
        return currentDate.after(certificate.getEndDate());
    }

    @Override
    public ArrayList<Certificate> getPredecessorsChain(Long certificateId){
        Certificate currentCertificate = certificateRepository.findById(certificateId)
                .orElseThrow(() -> new RuntimeException("Certificate not found."));
        ArrayList<Certificate> certificateChain = new ArrayList<Certificate>();

        do{
            certificateChain.add(currentCertificate);
            Long issuerId = currentCertificate.getIssuer().getId();
            currentCertificate = certificateRepository.findBySubjectId(issuerId)
                    .orElseThrow(() -> new RuntimeException("Certificate of issuer not found."));
            if(currentCertificate.getType() == CertType.ROOT){
                certificateChain.add(currentCertificate);
                break;
            }
        } while(currentCertificate.getIssuer().getId() != currentCertificate.getSubject().getId());

        Collections.reverse(certificateChain);

        System.out.println("------CERTIFICATE PREDECESSORS------");
        for (Certificate certificate : certificateChain) {
            System.out.println("Certificate ID: " + certificate.getId());
            System.out.println("Subject: " + certificate.getSubject().getName());
            System.out.println("Issuer: " + certificate.getIssuer().getName());
            System.out.println("Start Date: " + certificate.getStartDate());
            System.out.println("End Date: " + certificate.getEndDate());
            System.out.println("Type: " + certificate.getType());
            System.out.println("Public Key: " + certificate.getPublicKey());
            System.out.println("-----------------------------------");
        }
        return certificateChain;
    }

    public CertificateDto revokeCertificate(String certificateId){
        Certificate certificate = certificateRepository.findById(Long.parseLong(certificateId))
                .orElseThrow(() -> new RuntimeException("Certificate to revoke not found."));

        ArrayList<Certificate> successors = getSuccessorsChain(Long.parseLong(certificateId));
        certificate.setValid(false);
        for (Certificate c : successors) {
            c.setValid(false);
        }
        successors.add(certificate);
        certificateRepository.saveAll(successors);
        CertificateDto dto = new CertificateDto();
        dto.equals(certificate);
        return dto;
    }

    @Override
    public ArrayList<Certificate> getSuccessorsChain(Long certificateId){
        Certificate rootCertificate = certificateRepository.findById(certificateId)
                .orElseThrow(() -> new RuntimeException("Certificate not found."));
        ArrayList<Certificate> certificateChain = new ArrayList<>();
        Set<Long> visited = new HashSet<>();
        dfs(rootCertificate, certificateChain, visited);
        return certificateChain;
    }

    private void dfs(Certificate currentCertificate, List<Certificate> successors, Set<Long> visited) {
        successors.add(currentCertificate);
        visited.add(currentCertificate.getId());
        List<Certificate> childCertificates = certificateRepository.findByIssuerId(currentCertificate.getId());
        for (Certificate childCertificate : childCertificates) {
            if (!visited.contains(childCertificate.getId())) {
                dfs(childCertificate, successors, visited);
            }
        }
    }

    public boolean search(String param){
        Certificate certificate = certificateRepository.findBySerialNumber(BigInteger.valueOf(Long.parseLong(param)))
                .orElseThrow(() -> new RuntimeException("Certificate not found."));
        if(isExpired(certificate) || certificate.isValid() == false){ //if revoked before
            return false;
        } else {
            return  true;
        }
    }
}
