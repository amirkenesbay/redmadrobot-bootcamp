package com.amirkenesbay.services.impl;

import com.amirkenesbay.entity.Role;
import com.amirkenesbay.repository.RoleRepository;
import com.amirkenesbay.services.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log4j
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public Optional<Role> findByName(String name) {
        log.info("Get role by name: " + roleRepository.findRoleByName(name));
        return roleRepository.findRoleByName(name);
    }

    @Override
    public List<Role> findAll() {
        List<Role> list = new ArrayList<>();
        roleRepository.findAll().iterator().forEachRemaining(list::add);
        return list;
    }

    @Override
    public Role save(Role role) {
        return roleRepository.save(role);
    }
}
