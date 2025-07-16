package com.example.marketingagencymanagement.service;

import com.example.marketingagencymanagement.model.Permission;
import com.example.marketingagencymanagement.model.Role;
import com.example.marketingagencymanagement.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    private final RoleRepository roleRepository;
    private static final Logger logger= LoggerFactory.getLogger(UserService.class);

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public Optional<Role> getRoleById(Long id) {
        return roleRepository.findById(id);
    }

    public Role saveRole(Role role) {
        logger.info("Rola "+role.getName()+" uspesno sacuvana!");
        return roleRepository.save(role);
    }

    public void deleteRole(Long id) {
        roleRepository.deleteById(id);
        logger.info("Rola "+getRoleById(id).get().getName()+" uspesno izbrisana!");
    }
    public boolean deletePermissionForRole(Long id, String permissionName)
    {
        Role role=roleRepository.findById(id).get();
        for (Permission p:role.getPermissions()) {
            if(p.getName().equals(permissionName))
                role.getPermissions().remove(p);
        }
        roleRepository.save(role);
        return true;
    }
}
