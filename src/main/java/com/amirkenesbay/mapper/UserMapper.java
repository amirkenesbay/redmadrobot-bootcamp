package com.amirkenesbay.mapper;

import com.amirkenesbay.dto.UserDTO;
import com.amirkenesbay.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public User mapToEntity(UserDTO userDTO) {
        return User.builder()
                .email(userDTO.getEmail())
                .password(userDTO.getPassword())
                .isActive(userDTO.isActive())
                .build();
    }
}
