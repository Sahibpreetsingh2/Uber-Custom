package com.sahib.uberApp.UberApplication.contollers;

import com.sahib.uberApp.UberApplication.advises.ApiResponse;
import com.sahib.uberApp.UberApplication.dtos.LoginDto;
import com.sahib.uberApp.UberApplication.dtos.LoginResponseDto;
import com.sahib.uberApp.UberApplication.dtos.SignUpDto;
import com.sahib.uberApp.UberApplication.dtos.UserDto;
import com.sahib.uberApp.UberApplication.exception.ResourceNotFound;
import com.sahib.uberApp.UberApplication.services.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    @Value("${deploy.env}")
    private String env;

    private final AuthService authService;

    @PostMapping("/signUp")
    public UserDto signUpDto(@RequestBody SignUpDto signUpDto){
        return authService.signUp(signUpDto);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDto>> login(@RequestBody LoginDto loginDto,
                                                                HttpServletResponse response){
        LoginResponseDto loginResponseDto = authService.login(loginDto);

        Cookie cookie = new Cookie("refreshToken" , loginResponseDto.getRefreshToken());
        cookie.setHttpOnly(true);
        cookie.setSecure(env.equals("production")); // https
        response.addCookie(cookie);
        return ResponseEntity.ok(new ApiResponse<>(loginResponseDto));
    }

    @PostMapping("/refreshToken")
    public  ResponseEntity<ApiResponse<LoginResponseDto>> refreshToken(HttpServletRequest request) {

        Cookie[] cookies = request.getCookies();

        String refreshToken = Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals("refreshToken"))
                .map(Cookie::getValue)
                .findFirst()
                .orElseThrow(() -> new ResourceNotFound("Refresh Token is not Valid"));

        LoginResponseDto loginResponseDto = authService.generateAccessToken(refreshToken);
        return ResponseEntity.ok(new ApiResponse<>(loginResponseDto));
    }

//    @PostMapping("/logout")
//    public ResponseEntity<String> logout(HttpServletRequest request) {
//
//        String token = jwtService.extractToken(request);
//
//        authService.logout(token);
//
//        return ResponseEntity.ok("Logged out successfully");
//    }
}
