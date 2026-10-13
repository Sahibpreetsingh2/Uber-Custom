package com.sahib.uberApp.UberApplication.dtos;

import com.sahib.uberApp.UberApplication.enums.Roles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private String name;
    private String email;
    private Set<Roles> roles;
}
