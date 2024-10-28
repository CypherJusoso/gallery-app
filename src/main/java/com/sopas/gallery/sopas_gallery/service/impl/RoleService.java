package com.sopas.gallery.sopas_gallery.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sopas.gallery.sopas_gallery.entity.Role;
import com.sopas.gallery.sopas_gallery.repository.RoleRepository;
import com.sopas.gallery.sopas_gallery.service.interfac.IRoleService;

import jakarta.annotation.PostConstruct;

@Service
public class RoleService implements IRoleService {

    @Autowired
    private RoleRepository roleRepository;

    @PostConstruct
    public void init(){
        createRoleIfNotExists("ROLE_ADMIN");
        createRoleIfNotExists("ROLE_USER");

    }
    @Override
    public void createRoleIfNotExists(String roleName) {
        if(!roleRepository.findByName(roleName).isPresent()){
            Role role = new Role();
            role.setName(roleName);
            roleRepository.save(role);
        }
    }

}
