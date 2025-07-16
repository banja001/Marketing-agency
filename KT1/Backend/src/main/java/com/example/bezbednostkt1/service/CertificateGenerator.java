package com.example.bezbednostkt1.service;

import com.example.bezbednostkt1.data.IssuerData;
import com.example.bezbednostkt1.data.SubjectData;
import com.example.bezbednostkt1.model.CertType;
import com.example.bezbednostkt1.model.Role;
import com.example.bezbednostkt1.model.User;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;

import java.math.BigInteger;
import java.security.*;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class CertificateGenerator {
    public CertificateGenerator() {}

    public X509Certificate generateCertificate(SubjectData subjectData, IssuerData issuerData) {
        try {
            // potpis
            JcaContentSignerBuilder builder = new JcaContentSignerBuilder("SHA256WithRSAEncryption");
            BouncyCastleProvider bc = new BouncyCastleProvider();
            builder = builder.setProvider(bc);
            ContentSigner contentSigner = builder.build(issuerData.getPrivateKey());

            // pravljenje cert
            X509v3CertificateBuilder certGen = new JcaX509v3CertificateBuilder(issuerData.getX500name(),
                    new BigInteger(subjectData.getSerialNumber()),
                    subjectData.getStartDate(),
                    subjectData.getEndDate(),
                    subjectData.getX500name(),
                    subjectData.getPublicKey());
            X509CertificateHolder certHolder = certGen.build(contentSigner);

            // konvertovanje cert
            JcaX509CertificateConverter certConverter = new JcaX509CertificateConverter();
            certConverter = certConverter.setProvider(bc);
            return certConverter.getCertificate(certHolder);
        } catch (CertificateEncodingException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (OperatorCreationException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        }
        return null;
    }

    public SubjectData generateSubjectData(User user, PublicKey pubKey) {
        Date startDate = new Date();
        Date endDate = null;
        if(user.getRole().equals(Role.ADMIN)) {
            endDate = generateEndDate(startDate, CertType.ROOT);
        }
        if(user.getRole().equals(Role.INTERMEDIARY_CA)) {
            endDate = generateEndDate(startDate, CertType.INTERMEDIATE);
        }
        if(user.getRole().equals(Role.END_ENTITY)) {
            endDate = generateEndDate(startDate, CertType.END_ENTITY);
        }

        String serialNumber=generateSerialNumber();
        X500NameBuilder builder = generateData(user);

        return new SubjectData(pubKey, builder.build(), serialNumber, startDate, endDate);
    }


    public IssuerData generateIssuerData(User user, PrivateKey issuerKey) {
        X500NameBuilder builder = generateData(user);

        return new IssuerData(builder.build(), issuerKey);
    }

    private X500NameBuilder generateData(User user){
        X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
        builder.addRDN(BCStyle.CN, user.getName() + " " + user.getLastname());
        builder.addRDN(BCStyle.SURNAME, user.getName());
        builder.addRDN(BCStyle.GIVENNAME, user.getLastname());
        builder.addRDN(BCStyle.O, user.getOrganization());
        builder.addRDN(BCStyle.C, user.getCountry());
        builder.addRDN(BCStyle.E, user.getEmail());
        builder.addRDN(BCStyle.UID, String.valueOf(user.getId()));

        return builder;
    }

    public KeyPair generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
            keyGen.initialize(4096, random);
            return keyGen.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Date generateEndDate(Date startDate, CertType type){
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);

        if(type.equals(CertType.ROOT)){
            cal.add(Calendar.YEAR, 10);
        }
        if(type.equals(CertType.INTERMEDIATE)){
            cal.add(Calendar.YEAR, 5);
        }
        if(type.equals(CertType.END_ENTITY)){
            cal.add(Calendar.YEAR, 1);
        }
        return cal.getTime();
    }

    private String generateSerialNumber(){
        Random random = new Random();
        long randomNumber = Math.abs(random.nextLong());
        String randomString = Long.toString(randomNumber);
        String tenDigitNumber = randomString.substring(0, 10);
        return tenDigitNumber;
    }
}
