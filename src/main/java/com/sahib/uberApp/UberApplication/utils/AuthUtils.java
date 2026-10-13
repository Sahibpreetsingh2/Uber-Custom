package com.sahib.uberApp.UberApplication.utils;

import com.sahib.uberApp.UberApplication.entities.Users;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthUtils {

    public static Users getCurrentLoggedUser(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()){
            throw new RuntimeException("No current logged User");
        }
        return (Users) authentication.getPrincipal();

    }

}
