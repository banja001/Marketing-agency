package com.example.bezbednostkt1.repository;

import com.example.bezbednostkt1.model.CertType;
import com.example.bezbednostkt1.model.Certificate;
import com.example.bezbednostkt1.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Optional;

public interface CertificateRepository  extends JpaRepository<Certificate, Long> {
    @Query("SELECT c FROM Certificate c WHERE c.subject.id = :id AND c.isValid = true")
    Optional<Certificate> findBySubjectId(Long id);
    @Query("SELECT c FROM Certificate c WHERE c.subject.id = ?1  AND c.issuer.id=?2 ")
    Optional<Certificate> getIssuerRoot(Long subjectId,Long issuerId);

    @Query("SELECT c FROM Certificate c WHERE c.type  IN (:types) ")
    ArrayList<Certificate> findImIssuers(ArrayList<CertType> types);

    @Query("SELECT c FROM Certificate c WHERE c.issuer.id = :id AND c.isValid = true")
    ArrayList<Certificate> findByIssuerId(Long id);

    @Query("SELECT c FROM Certificate c WHERE c.serialNumber = :param")
    Optional<Certificate> findBySerialNumber(BigInteger param);
}
