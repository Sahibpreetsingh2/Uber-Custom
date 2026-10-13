package com.sahib.uberApp.UberApplication.services.impl;

//import com.pisoft.uberApp.UberApplication.dtos.*;
import com.sahib.uberApp.UberApplication.dtos.*;
import com.sahib.uberApp.UberApplication.entities.Driver;
import com.sahib.uberApp.UberApplication.entities.Users;
import com.sahib.uberApp.UberApplication.enums.Roles;
import com.sahib.uberApp.UberApplication.exception.ResourceNotFound;
import com.sahib.uberApp.UberApplication.repositories.UserRepository;
//import com.pisoft.uberApp.UberApplication.services.*;
import com.sahib.uberApp.UberApplication.services.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.Set;

import static com.sahib.uberApp.UberApplication.enums.Roles.DRIVER;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final RiderService riderService ;
    private final WalletService walletService;
    private final UserService userService;
    private final DriverService driverService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserSessionService userSessionService;
    private final PasswordEncoder passwordEncoder;
    private final RedisService redisService;



    @Override
    public UserDto signUp(SignUpDto signUpDto) {

        Optional<Users> optional = userRepository.findByEmail(signUpDto.getEmail());

        if (optional.isPresent()){
            throw new ResourceNotFound("User Already Exist ....");
        }

        // user creation :
        Users users = modelMapper.map(signUpDto, Users.class);
        users.setRoles(Set.of(Roles.RIDER));
        users.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
        Users saveUser = userRepository.save(users);

        // rider creation :
        riderService.createNewRider(saveUser);

        // Wallet Creation:
        walletService.createNewWallet(saveUser);
        return modelMapper.map(saveUser , UserDto.class);
    }

    @Override
    public LoginResponseDto login(LoginDto loginDto) {

        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getEmail(), loginDto.getPassword()
                )
        );
        Users users = (Users) authenticate.getPrincipal();
        String accessToken = jwtService.generateAccessToken(users);
        String refreshToken = jwtService.generateRefreshToken(users);

        userSessionService.generateUserSession(users , refreshToken);
        return new LoginResponseDto(users.getId() , accessToken , refreshToken);
    }

//    @Override
//    public void logOut(String token) {
//        long expiration = jwtService.getRemainingExpiration(token);
//
//        redisService.set(
//                "jwt:blacklist" + token,
//                true,
//                expiration,
//                TimeUnit.MILLISECONDS
//        );
//    }

    @Override
    public DriverDto onBoardNewDriver(DriverOnboardDto driverOnboardDto) {

        Users users = userService.getCurrentLoggedUser();

        if (users.getRoles().contains(DRIVER)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST ,
                    "User is already exist as a DRIVER role");
        }

        Driver driver = Driver
                .builder()
                .available(true)
                .averageRating(0.0)
                .licNo(driverOnboardDto.getLicNo())
                .totalRating(0)
                .users(users)
                .build();

        Driver driver1 = driverService.onBoardNewDriver(driver);

        users.getRoles().add(DRIVER);
        userService.save(users);
        return modelMapper.map(driver1, DriverDto.class);
    }

    public LoginResponseDto generateAccessToken(String refreshToken) {

        Long userIdFromToken = jwtService.generateUserIdFromToken(refreshToken);
        userSessionService.validateUserSession(refreshToken);
        Users users = userService.findById(userIdFromToken);

        String accessToken = jwtService.generateAccessToken(users);
        return new LoginResponseDto(users.getId() , accessToken , refreshToken);
    }
}
