package com.sahib.uberApp.UberApplication.services;

//import com.pisoft.uberApp.UberApplication.dtos.*;
import com.sahib.uberApp.UberApplication.dtos.*;

public interface AuthService {

    UserDto signUp(SignUpDto signUpDto);

    LoginResponseDto login(LoginDto loginDto);

//    void logOut(String token); // spring security :

    DriverDto onBoardNewDriver(DriverOnboardDto driverOnboardDto);

    LoginResponseDto generateAccessToken(String refreshToken);
}
