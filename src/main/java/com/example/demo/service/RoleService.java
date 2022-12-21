package com.example.demo.service;

import com.example.demo.models.Role;
import com.example.demo.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class RoleService {
    @Autowired
    RoleRepository roleRepository;
    Optional<Role> findByName(String name){
        return roleRepository.findByName(name);
    }
}
