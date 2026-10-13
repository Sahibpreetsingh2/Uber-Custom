package com.sahib.uberApp.UberApplication.services;

import com.sahib.uberApp.UberApplication.entities.Driver;
import com.sahib.uberApp.UberApplication.entities.Ride;
import com.sahib.uberApp.UberApplication.entities.RideRequest;
import com.sahib.uberApp.UberApplication.entities.Rider;
import com.sahib.uberApp.UberApplication.enums.RideStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RideService {

    Ride getById(Long rideId);
    Ride createNewRide(RideRequest rideRequest , Driver driver);
    Ride updateRideStatus(Ride ride , RideStatus rideStatus);

    Page<Ride> getAllRidesOfRider(Rider rider , Pageable pageable);
    Page<Ride> getAllRidesOfDriver(Driver driver , Pageable pageable);

    Ride saveRide(Ride ride);
}
