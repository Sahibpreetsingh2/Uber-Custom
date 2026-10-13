package com.sahib.uberApp.UberApplication.contollers;

import com.sahib.uberApp.UberApplication.dtos.AdminUserDto;
import com.sahib.uberApp.UberApplication.services.AdminService;
import com.sahib.uberApp.UberApplication.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final UserService userService;


//    @GetMapping("/all-users")
//    public ResponseEntity<List<Users>> getAllUsers() {
//
//        return ResponseEntity.ok(userService.getAll());
//    }

    @GetMapping("/all-users")
    public ResponseEntity<List<AdminUserDto>> getAllUsers() {

        return ResponseEntity.ok(userService.getAllUsers());
    }


    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping("/grant-admin/{userId}")
    public ResponseEntity<String> grantAdmin(
            @PathVariable Long userId) {

        adminService.grantAdmin(userId);

        return ResponseEntity.ok(
                "Admin role granted successfully."
        );
    }


    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping("/remove-admin/{userId}")
    public ResponseEntity<String> removeAdmin(
            @PathVariable Long userId) {

        adminService.removeAdmin(userId);

        return ResponseEntity.ok(
                "Admin role removed successfully."
        );
    }


    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PostMapping("/transfer-ownership")
    public ResponseEntity<String> transferOwnership(
            @RequestParam Long newOwnerId) {

        adminService.transferOwnership(newOwnerId);

        return ResponseEntity.ok(
                "Ownership transferred successfully."
        );
    }


    @GetMapping("/dashboard")
    public ResponseEntity<String> dashboard() {

        return ResponseEntity.ok(
                "Admin dashboard working"
        );
    }
}