package com.sahib.uberApp.UberApplication.config;

import com.sahib.uberApp.UberApplication.entities.Users;
import com.sahib.uberApp.UberApplication.enums.Roles;
import com.sahib.uberApp.UberApplication.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args){
        boolean exists =
                userRepository.existsByRolesContains(Roles.SUPER_ADMIN);

        if (exists) {
            return;
        }

        Users admin = Users.builder()
                .name("Sahibpreet Singh")
                .email("sahibpreetsingh432@gmail.com")
                .password(passwordEncoder.encode("Sahib2005"))
                .roles(Set.of(Roles.SUPER_ADMIN))
                .build();
        userRepository.save(admin);

    }
}
