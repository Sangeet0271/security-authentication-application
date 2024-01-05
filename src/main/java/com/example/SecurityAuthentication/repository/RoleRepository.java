package com.example.SecurityAuthentication.repository;

import java.util.Optional;

import com.example.SecurityAuthentication.Entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RoleRepository extends JpaRepository<Role, Integer> {
    public Optional<Role> findByName(String name);
}