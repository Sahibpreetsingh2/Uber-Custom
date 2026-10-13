package com.sahib.uberApp.UberApplication.strategies;

import com.sahib.uberApp.UberApplication.entities.RideRequest;

public interface RideFareCalculationStrategy {

    Double calculateFare(RideRequest rideRequest);


}
