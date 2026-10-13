package com.sahib.uberApp.UberApplication.services.impl;

import com.sahib.uberApp.UberApplication.dtos.AdminUserDto;
import com.sahib.uberApp.UberApplication.entities.Users;
import com.sahib.uberApp.UberApplication.exception.ResourceNotFound;
import com.sahib.uberApp.UberApplication.repositories.UserRepository;
import com.sahib.uberApp.UberApplication.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService  , UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public Users getCurrentLoggedUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()){
            throw new RuntimeException("No current logged User");
        }
        return (Users) authentication.getPrincipal();
    }

    @Override
    public Users findById(Long userId) {
        return userRepository.findById(userId).orElseThrow(()->
                new ResourceNotFound("User not found with Id : " + userId));
    }

    @Override
    public void save(Users users) {
        userRepository.save(users);
    }

    @Override
    public Users findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(()->
                new ResourceNotFound("Email not exist : "+email));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return findByEmail(username);
    }

//    @Override
//    public List<Users> getAll(){
//        return userRepository.findAll();
//    }

    @Override
    public List<AdminUserDto> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(user -> AdminUserDto.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .email(user.getEmail())
                        .roles(user.getRoles())
                        .build())
                .toList();
    }

}
