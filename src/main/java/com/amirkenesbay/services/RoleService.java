package com.amirkenesbay.services;

import com.amirkenesbay.entity.Role;

import java.util.List;
import java.util.Optional;

public interface RoleService {
    Optional<Role> findByName(String name);

    List<Role> findAll();

    Role save(Role role);
}
