package com.sahib.uberApp.UberApplication.strategies;

import com.sahib.uberApp.UberApplication.entities.Driver;
import com.sahib.uberApp.UberApplication.entities.RideRequest;

import java.util.List;

public interface DriverMatchingStrategy {
    List<Driver> matchDrivers(RideRequest rideRequest);
}

