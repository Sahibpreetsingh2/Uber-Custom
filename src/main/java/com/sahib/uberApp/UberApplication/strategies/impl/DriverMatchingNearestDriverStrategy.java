package com.sahib.uberApp.UberApplication.strategies.impl;

import com.sahib.uberApp.UberApplication.entities.Driver;
import com.sahib.uberApp.UberApplication.entities.RideRequest;
import com.sahib.uberApp.UberApplication.repositories.DriverRepository;
import com.sahib.uberApp.UberApplication.services.RedisGeoService;
import com.sahib.uberApp.UberApplication.strategies.DriverMatchingStrategy;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

//@Service
//@RequiredArgsConstructor
//public class DriverMatchingNearestDriverStrategy implements DriverMatchingStrategy {
//
//    private final DriverRepository driverRepository;
//
//    @Override
//    public List<Driver> matchDrivers(RideRequest rideRequest) {
//        return driverRepository.findTopNearestDriver(rideRequest.getPickUpLocation());
//    }
//}
@Service
@RequiredArgsConstructor
public class DriverMatchingNearestDriverStrategy
        implements DriverMatchingStrategy {

    private final RedisGeoService redisGeoService;
    private final DriverRepository driverRepository;

    @Override
    public List<Driver> matchDrivers(
            RideRequest rideRequest) {

        org.locationtech.jts.geom.Point pickup =
                rideRequest.getPickUpLocation();

        if (pickup == null) {
            return List.of();
        }

        double longitude = pickup.getX();
        double latitude = pickup.getY();

        List<Long> driverIds =
                redisGeoService.findNearbyDrivers(
                        latitude,
                        longitude,
                        5
                );

        List<Driver> drivers = new ArrayList<>();

        for (Long driverId : driverIds) {

            Driver driver =
                    driverRepository.findById(driverId)
                            .orElse(null);

            if (driver != null
                    && Boolean.TRUE.equals(driver.getAvailable())) {

                drivers.add(driver);
            }
        }

        return drivers;
    }
}


