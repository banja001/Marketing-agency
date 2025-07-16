package com.example.bezbednostkt1.service;

import com.example.bezbednostkt1.data.IssuerData;
import com.example.bezbednostkt1.data.SubjectData;
import com.example.bezbednostkt1.dto.CertificateDto;
import com.example.bezbednostkt1.dto.CreateCertificateDto;
import com.example.bezbednostkt1.dto.KeyPairDto;
import com.example.bezbednostkt1.model.CertType;
import com.example.bezbednostkt1.model.Certificate;
import com.example.bezbednostkt1.model.Role;
import com.example.bezbednostkt1.model.User;
import com.example.bezbednostkt1.pki.keystores.KeyStoreReader;
import com.example.bezbednostkt1.pki.keystores.KeyStoreWriter;
import com.example.bezbednostkt1.repository.CertificateRepository;
import com.example.bezbednostkt1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;


@Service
public class IMCertificateServiceImpl implements IMCertificateService{
    private CertificateGenerator generator = new CertificateGenerator();

    @Autowired
    private CertificateRepository certificateRepository;
    @Autowired
    private UserRepository userRepository;
    @Override
    public ArrayList<Certificate> getPossibleCertificates() {
        ArrayList<CertType> types = new ArrayList<>();
        types.add(CertType.INTERMEDIATE);
        types.add(CertType.ROOT);
        ArrayList<Certificate> certificates = certificateRepository.findImIssuers(types);
        certificates.removeIf(certificate -> !certificate.isValid());
        return certificates;
    }

    @Override
    public KeyPairDto createCertificate(CreateCertificateDto certDto) {
        PublicKey subjectPubKey = null;
        PrivateKey subjectPrivKey = null;
        if(certDto.getPrivateKey().isEmpty() || certDto.getPublicKey().isEmpty()){
            KeyPair keyPair = generator.generateKeyPair();
            subjectPubKey = keyPair.getPublic();
            subjectPrivKey = keyPair.getPrivate();
        }
        if(!certDto.getPrivateKey().isEmpty() && !certDto.getPublicKey().isEmpty()){
            subjectPrivKey = stringToPrivateKey(certDto.getPrivateKey());
            subjectPubKey = stringToPublicKey(certDto.getPublicKey());

        }

        Optional<User> subject = userRepository.findByUsername(certDto.getSubjectUsername());
        if (subject.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "message: Could not find the subject!");
        }

        Optional<User> issuer = userRepository.findByUsername(certDto.getIssuerUsername());
        if (issuer.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "message: Could not find the issuer!");
        }

        SubjectData subjectData = generator.generateSubjectData(subject.get(), subjectPubKey);
        //nadji priv kljuc

        KeyStoreReader keyStoreReader=new KeyStoreReader();
        KeyStoreWriter keyStoreWriter=new KeyStoreWriter();
        if(issuer.get().getRole()==Role.ADMIN)
        {
            PrivateKey privateKeyIs = keyStoreReader.readPrivateKey("root_" + getRootCertificate(issuer.get().getId().toString()).get().getSerialNumber() + "_" + issuer.get().getId() + ".jks", (issuer.get().getName() + issuer.get().getId()), getRootCertificate(issuer.get().getId().toString()).get().getSerialNumber().toString(), (issuer.get().getName() + issuer.get().getId()));
            //System.out.println(privateKeyIs);

            IssuerData issuerData = generator.generateIssuerData(issuer.get(), privateKeyIs);

            X509Certificate cert = generator.generateCertificate(subjectData, issuerData);

            certificateRepository.save(createDatabaseCertificate(subject.get(), issuer.get(), subjectPubKey, cert.getSerialNumber()));
            keyStoreWriter.loadKeyStore("root_"+getRootCertificate(issuer.get().getId().toString()).get().getSerialNumber()+"_"+issuer.get().getId()+".jks",(issuer.get().getName()+issuer.get().getId()).toCharArray());
            keyStoreWriter.write(cert.getSerialNumber().toString(),subjectPrivKey,(issuer.get().getName()+issuer.get().getId()).toCharArray(),cert);
            keyStoreWriter.saveKeyStore("root_"+getRootCertificate(issuer.get().getId().toString()).get().getSerialNumber()+"_"+issuer.get().getId()+".jks",(issuer.get().getName()+issuer.get().getId()).toCharArray());

        }
        if(issuer.get().getRole()==Role.INTERMEDIARY_CA)
        {
            //Certificate c=getIssuerRoot(subject.get().getId(),issuer.get().getId()).get();
            Certificate c= getRootCertificate(issuer.get().getId().toString()).get();
            String rootId=getRootCertificate(issuer.get().getId().toString()).get().getIssuer().getId().toString();
            String rootSerial=getRootCertificate(rootId).get().getSerialNumber().toString();
            String rootName=getRootCertificate(rootId).get().getIssuer().getName();
            System.out.println(rootSerial);
            System.out.println(rootId);
            PrivateKey privateKeyIs=null;
            if(getRootCertificate(rootId).get().getIssuer().getRole()==Role.ADMIN)
                privateKeyIs = keyStoreReader.readPrivateKey("root_" + rootSerial + "_" + rootId + ".jks", (rootName+rootId),rootSerial, (rootName+rootId));
            //System.out.println(privateKeyIs);
            else if(getRootCertificate(rootId).get().getIssuer().getRole()==Role.INTERMEDIARY_CA)
                privateKeyIs = keyStoreReader.readPrivateKey("inter_" + rootSerial + "_" + rootId + ".jks", (rootName+rootId),rootSerial, (rootName+rootId));

            IssuerData issuerData = generator.generateIssuerData(issuer.get(), privateKeyIs);

            X509Certificate cert = generator.generateCertificate(subjectData, issuerData);

            certificateRepository.save(createDatabaseCertificate(subject.get(), issuer.get(), subjectPubKey, cert.getSerialNumber()));
            if(keyStoreWriter.loadKeyStore("inter_"+getRootCertificate(issuer.get().getId().toString()).get().getSerialNumber()+"_"+issuer.get().getId()+".jks",(issuer.get().getName()+issuer.get().getId()).toCharArray()))
            {
                keyStoreWriter.write(cert.getSerialNumber().toString(),subjectPrivKey,(issuer.get().getName()+issuer.get().getId()).toCharArray(),cert);

                keyStoreWriter.saveKeyStore("inter_"+getRootCertificate(issuer.get().getId().toString()).get().getSerialNumber()+"_"+issuer.get().getId()+".jks",(issuer.get().getName()+issuer.get().getId()).toCharArray());

            }
            else
            {
                keyStoreWriter.loadKeyStore(null,(issuer.get().getName()+issuer.get().getId()).toCharArray());
                keyStoreWriter.write(cert.getSerialNumber().toString(),subjectPrivKey,(issuer.get().getName()+issuer.get().getId()).toCharArray(),cert);
                keyStoreWriter.saveKeyStore("inter_"+getRootCertificate(issuer.get().getId().toString()).get().getSerialNumber()+"_"+issuer.get().getId()+".jks",(issuer.get().getName()+issuer.get().getId()).toCharArray());

            }
        }

        return new KeyPairDto(Base64.getEncoder().encodeToString(subjectPrivKey.getEncoded()),
                Base64.getEncoder().encodeToString(subjectPubKey.getEncoded()));
    }

    private PublicKey stringToPublicKey(String pubKey){
        try {
            pubKey = pubKey.replaceAll("\\s", "");
            byte[] publicBytes = Base64.getDecoder().decode(pubKey);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(keySpec);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private PrivateKey stringToPrivateKey(String privKey){
        try {
            privKey = privKey.replaceAll("\\s", "");
            byte[] privateBytes = Base64.getDecoder().decode(privKey);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(keySpec);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private Certificate createDatabaseCertificate(User subject, User issuer, PublicKey pubKey, BigInteger serNum){
        Certificate cert = new Certificate();

        cert.setSubject(subject);
        cert.setIssuer(issuer);
        cert.setPublicKey(Base64.getEncoder().encodeToString(pubKey.getEncoded()));
        cert.setSerialNumber(serNum);
        Date today = new Date();
        cert.setStartDate(today);
        cert.setValid(true);

        if(subject.getRole().equals(Role.INTERMEDIARY_CA)) {
            Date endDate = generator.generateEndDate(today, CertType.INTERMEDIATE);
            cert.setEndDate(endDate);

            cert.setType(CertType.INTERMEDIATE);
        }
        if(subject.getRole().equals(Role.END_ENTITY)) {
            Date endDate = generator.generateEndDate(today, CertType.END_ENTITY);
            cert.setEndDate(endDate);

            cert.setType(CertType.END_ENTITY);
        }


        return cert;
    }

    public Optional<Certificate> getRootCertificate(String subjectId) {

        Optional<Certificate> cert = certificateRepository.findBySubjectId(Long.valueOf(subjectId));
        if (cert.isEmpty()) {
            return null;
        }
        return cert;
    }
    public Optional<Certificate> getIssuerRoot(Long subjectId,Long issuerId)
    {
            return certificateRepository.getIssuerRoot(subjectId, issuerId);
    }
}
