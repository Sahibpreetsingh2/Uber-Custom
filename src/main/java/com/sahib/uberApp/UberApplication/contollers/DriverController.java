package com.sahib.uberApp.UberApplication.contollers;

import com.sahib.uberApp.UberApplication.dtos.DriverDto;
import com.sahib.uberApp.UberApplication.dtos.DriverOnboardDto;
import com.sahib.uberApp.UberApplication.dtos.RideDto;
import com.sahib.uberApp.UberApplication.dtos.RideStartDto;
import com.sahib.uberApp.UberApplication.entities.Users;
//import com.pisoft.uberApp.UberApplication.services.*;
import com.sahib.uberApp.UberApplication.services.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/driver")
@RequiredArgsConstructor
public class DriverController {

    private final DriverService driverService;
    private final RideService rideService;
    private final AuthService authService;
    private final RedisGeoService redisGeoService;
    private final UserService userService;

    @PostMapping("/acceptRide/{rideRequestId}")
    public ResponseEntity<RideDto> acceptRide(@PathVariable Long rideRequestId){
        RideDto rideDto = driverService.acceptRide(rideRequestId);
        return new ResponseEntity<>(rideDto , HttpStatus.CREATED);
    }

    @PostMapping("/startRide/{rideId}")
    public ResponseEntity<RideDto> startRide(@PathVariable Long rideId , @RequestBody RideStartDto rideStartDto){
        RideDto rideDto = driverService.startRide(rideId, rideStartDto.getOtp());
        return new ResponseEntity<>(rideDto , HttpStatus.CREATED);
    }

    @PostMapping("/endRide/{rideId}")
    public ResponseEntity<RideDto> endRide(@PathVariable Long rideId){
        RideDto rideDto = driverService.endRide(rideId);
        return ResponseEntity.ok(rideDto);
    }

    @GetMapping("/getAllMyRides/{pageNo}")
    public ResponseEntity<List<RideDto>> getAllMyRides(@RequestParam(defaultValue = "0") int pageNo){
        return ResponseEntity.ok(driverService.getAllMyRides(pageNo).getContent());
    }

    @GetMapping("/getMyProfile")
    public ResponseEntity<DriverDto> getMyProfile(){
        return ResponseEntity.ok(driverService.getMyProfile());
    }

    @PostMapping("/onBoardNewDriver")
    public ResponseEntity<DriverDto> onBoardNewDriver(@RequestBody DriverOnboardDto driverOnboardDto){
        DriverDto driverDto = authService.onBoardNewDriver(driverOnboardDto);
        return new ResponseEntity<>(driverDto , HttpStatus.CREATED);
    }

    @PostMapping("/location")
    public ResponseEntity<String> updateLocation(
            @RequestParam double latitude,
            @RequestParam double longitude) {

        Users user = userService.getCurrentLoggedUser();

        Long driverId = user.getDriver().getId();

        redisGeoService.updateDriverLocation(
                driverId,
                latitude,
                longitude
        );

        return ResponseEntity.ok("Driver location updated");
    }
    @GetMapping("/nearby")
    public ResponseEntity<List<Long>> nearbyDrivers(
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam double radiusKm) {

        return ResponseEntity.ok(
                redisGeoService.findNearbyDrivers(
                        latitude,
                        longitude,
                        radiusKm
                )
        );
    }

}
