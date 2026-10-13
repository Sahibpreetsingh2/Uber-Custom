package com.sahib.uberApp.UberApplication.services.impl;

import com.sahib.uberApp.UberApplication.entities.Users;
import com.sahib.uberApp.UberApplication.enums.Roles;
import com.sahib.uberApp.UberApplication.repositories.UserRepository;
import com.sahib.uberApp.UberApplication.services.AdminService;
import com.sahib.uberApp.UberApplication.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final UserService userService;

    @Override
    public void grantAdmin(Long userId) {

        Users user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        user.getRoles().add(Roles.ADMIN);

        userRepository.save(user);
    }


    @Override
    public void removeAdmin(Long userId) {

        Users user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        user.getRoles().remove(Roles.ADMIN);

        userRepository.save(user);
    }


    @Override
    public void transferOwnership(Long newOwnerId) {

        Users currentOwner = userService.getCurrentLoggedUser();

        Users newOwner = userRepository.findById(newOwnerId)
                .orElseThrow(() ->
                        new RuntimeException("New owner not found"));

        // Make new user SUPER_ADMIN
        newOwner.getRoles().add(Roles.SUPER_ADMIN);

        // Remove SUPER_ADMIN from current owner
        currentOwner.getRoles().remove(Roles.SUPER_ADMIN);

        userRepository.save(currentOwner);
        userRepository.save(newOwner);
    }
}