package com.example.marketingagencymanagement.repository;

import com.example.marketingagencymanagement.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository  extends JpaRepository<Permission, Long> {
}
