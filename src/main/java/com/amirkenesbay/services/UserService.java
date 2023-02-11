package com.amirkenesbay.services;

import com.amirkenesbay.dto.UserDTO;
import com.amirkenesbay.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User save(UserDTO user);

    List<User> findAll();

    Optional<User> findByEmail(String email);

    Optional<User> findById(Long id);
}
