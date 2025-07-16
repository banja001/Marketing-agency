package com.example.marketingagencymanagement.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.util.Base64;

@Service
public class EncryptionService {

    @Value("${encryption.algorithm}")
    private String encryptionAlgorithm;

    @Value("${keystore.path}")
    private String keystorePath;

    @Value("${keystore.password}")
    private String keystorePassword;

    @Value("${aes.key.alias}")
    private String aesKeyAlias;

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        try {
            // Load the keystore
            KeyStore keystore = KeyStore.getInstance("PKCS12");
            InputStream keystoreStream = new FileInputStream(keystorePath);
            keystore.load(keystoreStream, keystorePassword.toCharArray());
            keystoreStream.close();

            // Retrieve the AES key
            secretKey = (SecretKey) keystore.getKey(aesKeyAlias, keystorePassword.toCharArray());

            if (secretKey == null) {
                throw new RuntimeException("Failed to retrieve AES key from keystore");
            }

            System.out.println("Encryption key successfully initialized from keystore.");
        } catch (Exception e) {
            System.err.println("Failed to initialize encryption key: " + e.getMessage());
            throw new RuntimeException("Failed to initialize encryption key", e);
        }
    }

    public String encrypt(String data) {
        try {
            Cipher cipher = Cipher.getInstance(encryptionAlgorithm);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedData = cipher.doFinal(data.getBytes("UTF-8"));
            return Base64.getEncoder().encodeToString(encryptedData);
        } catch (Exception e) {
            System.err.println("Encryption error: " + e.getMessage());
            throw new RuntimeException("Encryption error", e);
        }
    }

    public String decrypt(String encryptedData) {
        try {
            Cipher cipher = Cipher.getInstance(encryptionAlgorithm);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decodedData = Base64.getDecoder().decode(encryptedData);
            byte[] decryptedData = cipher.doFinal(decodedData);
            return new String(decryptedData, "UTF-8");
        } catch (Exception e) {
            System.err.println("Decryption error: " + e.getMessage());
            throw new RuntimeException("Decryption error", e);
        }
    }
}

