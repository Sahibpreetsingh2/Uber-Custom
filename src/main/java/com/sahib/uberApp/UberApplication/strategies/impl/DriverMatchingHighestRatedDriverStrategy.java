package com.sahib.uberApp.UberApplication.strategies.impl;

import com.sahib.uberApp.UberApplication.entities.Driver;
import com.sahib.uberApp.UberApplication.entities.RideRequest;
import com.sahib.uberApp.UberApplication.repositories.DriverRepository;
import com.sahib.uberApp.UberApplication.strategies.DriverMatchingStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DriverMatchingHighestRatedDriverStrategy implements DriverMatchingStrategy {

    private final DriverRepository driverRepository;

    @Override
    public List<Driver> matchDrivers(RideRequest rideRequest) {
        return driverRepository.findTopRatedDrivers(rideRequest.getPickUpLocation());
    }
}

