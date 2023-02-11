package com.amirkenesbay.services.impl;

import com.amirkenesbay.dto.UserDTO;
import com.amirkenesbay.entity.Role;
import com.amirkenesbay.entity.User;
import com.amirkenesbay.enums.RolesName;
import com.amirkenesbay.exception.UserEmailNotFoundException;
import com.amirkenesbay.mapper.UserMapper;
import com.amirkenesbay.repository.UserRepository;
import com.amirkenesbay.services.RoleService;
import com.amirkenesbay.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Log4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final RoleService roleService;

    @Override
    public User save(UserDTO userDto) {
        log.info("Saving user - start:" + userDto.toString());
        if(!userDto.getEmail().isBlank() && !isEmailIsUsed(userDto.getEmail())) {
            User user = userMapper.mapToEntity(userDto);
            user.setActive(true);
            Set<Role> roleSet = new HashSet<>();

            if(userDto.getRoles() != null && userDto.getRoles().length > 0) {
                Arrays.stream(userDto.getRoles()).forEach(
                        name -> {
                            Optional<Role> role = roleService.findByName(name);
                            role.ifPresent(roleSet::add);
                        }
                );
            }
            if(roleSet.isEmpty()) {
                Optional<Role> role = roleService.findByName(RolesName.USER.toString());
                role.ifPresent(roleSet::add);
            }

            user.setRoles(roleSet);
            log.info("Saving user - end:" + userDto);
            return userRepository.save(user);
        }
        log.error("Saving user - error:" + userDto);
        throw new UserEmailNotFoundException("User email is not found");
    }

    @Override
    public List<User> findAll() {
        List<User> list = new ArrayList<>();
        userRepository.findAll();
        log.info("Get all users:" + list);
        return list;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        log.info("Get all users by email:" + userRepository.findByEmail(email));
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findById(Long id) {
        log.info("Get user by id:" + userRepository.findById(id));
        return userRepository.findById(id);
    }

    private boolean isEmailIsUsed(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}
