package com.sahib.uberApp.UberApplication.contollers;

import com.sahib.uberApp.UberApplication.dtos.LiveLocationDto;
import com.sahib.uberApp.UberApplication.dtos.LocationUpdateDto;
import com.sahib.uberApp.UberApplication.entities.Driver;
import com.sahib.uberApp.UberApplication.entities.Ride;
import com.sahib.uberApp.UberApplication.entities.Rider;
import com.sahib.uberApp.UberApplication.services.DriverService;
import com.sahib.uberApp.UberApplication.services.RedisGeoService;
import com.sahib.uberApp.UberApplication.services.RideService;
import com.sahib.uberApp.UberApplication.services.RiderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LiveLocationController {
    private final RedisGeoService redisGeoService;
    private final RideService rideService;
    private final DriverService driverService;
    private final RiderService riderService;
    private final SimpMessagingTemplate messagingTemplate;

    @PostMapping("/driver/rides/{rideId}/location")
    public ResponseEntity<String> updateDriverLocation(
            @PathVariable Long rideId,
            @RequestBody LocationUpdateDto location){

        Driver driver = driverService.getCurrentDriver();
        Ride ride = rideService.getById(rideId);

        if(!ride.getDriver().equals(driver)){
            return ResponseEntity.status(403)
                    .body("This ride does not belong to this driver");
        }

        redisGeoService.updateDriverLocation(
                driver.getId(),
                location.getLatitude(),
                location.getLongitude()
        );


        LiveLocationDto liveLocation = new LiveLocationDto(
                driver.getId(),
                "DRIVER",
                location.getLatitude(),
                location.getLongitude()
        );

        messagingTemplate.convertAndSend(
                "/topic/ride/" + rideId + "/driver-location",
                liveLocation
        );

        return ResponseEntity.ok(
                "Driver location updated"
        );
    }

    @PostMapping("/rider/rides/{rideId}/location")
    public ResponseEntity<String> updateRideLocation(
            @PathVariable Long rideId,
            @RequestBody LocationUpdateDto location
            ){
        Rider rider = riderService.getCurrentRider();

        Ride ride = rideService.getById(rideId);

        if(!ride.getRider().equals(rider)){
            return ResponseEntity.status(403)
                    .body("This ride does not belong to this rider");
        }

        LiveLocationDto liveLocation = new LiveLocationDto(
                rider.getId(),
                "RIDER",
                location.getLatitude(),
                location.getLongitude()
        );
        messagingTemplate.convertAndSend(
                "/topic/ride/" + rideId + "/rider-location",
                liveLocation
        );

        return ResponseEntity.ok(
                "Rider location updated"
        );


    }



}
