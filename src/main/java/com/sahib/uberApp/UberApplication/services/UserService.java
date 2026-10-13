package com.sahib.uberApp.UberApplication.services;

import com.sahib.uberApp.UberApplication.dtos.AdminUserDto;
import com.sahib.uberApp.UberApplication.entities.Users;

import java.util.List;

public interface UserService {

    Users getCurrentLoggedUser();

    Users findById(Long userId);

    void save(Users users);

    Users findByEmail(String email);

    List<AdminUserDto> getAllUsers();
}
