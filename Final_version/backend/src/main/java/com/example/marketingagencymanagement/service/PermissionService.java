package com.example.marketingagencymanagement.service;
import com.example.marketingagencymanagement.model.Permission;
import com.example.marketingagencymanagement.repository.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@Transactional
public class PermissionService {

    private final PermissionRepository permissionRepository;
    private static final Logger logger= LoggerFactory.getLogger(UserService.class);
    @Autowired
    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public List<Permission> getAllPermissions() {
        return permissionRepository.findAll();
    }

    public Optional<Permission> getPermissionById(Long id) {
        return permissionRepository.findById(id);
    }

    public Permission createPermission(Permission permission) {
        logger.info("Permisija "+permission.getName()+" uspesno kreirana!");
        return permissionRepository.save(permission);
    }

    public Permission updatePermission(Long id, Permission updatedPermission) {
        Optional<Permission> existingPermission = permissionRepository.findById(id);
        if (existingPermission.isPresent()) {
            Permission permission = existingPermission.get();
            permission.setName(updatedPermission.getName());
            permission.setDescription(updatedPermission.getDescription());
            logger.info("Permisija "+updatedPermission.getName()+" uspesno azurirana!");
            return permissionRepository.save(permission);
        } else {
           // System.out.println("Greska prilikom update-ovanja permisije!");
            logger.error("Permisija "+updatedPermission.getName()+" nije azurirana!");
            return null;
        }
    }

    public void deletePermission(Long id) {
        logger.info("Permisija "+getPermissionById(id).get().getName()+" uspesno obrisana!");
        permissionRepository.deleteById(id);
    }
}
