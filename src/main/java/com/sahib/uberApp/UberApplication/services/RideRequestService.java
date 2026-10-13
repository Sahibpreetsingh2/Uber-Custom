package com.sahib.uberApp.UberApplication.services;

import com.sahib.uberApp.UberApplication.entities.RideRequest;

public interface RideRequestService {

    RideRequest findByRideRequestId(Long rideRequestId);

}
