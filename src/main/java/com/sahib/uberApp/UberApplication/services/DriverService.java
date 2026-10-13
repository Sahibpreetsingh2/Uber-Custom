package com.sahib.uberApp.UberApplication.services;

import com.sahib.uberApp.UberApplication.dtos.DriverDto;
import com.sahib.uberApp.UberApplication.dtos.RideDto;
import com.sahib.uberApp.UberApplication.entities.Driver;
import org.springframework.data.domain.Page;

public interface DriverService {

    RideDto acceptRide(Long requestedRideId);
    RideDto cancelRide(Long rideId );
    RideDto startRide(Long rideId, String otp);
    RideDto endRide(Long rideId);
    DriverDto getMyProfile(); //
    Driver getCurrentDriver();
    void updateDriverAvailability(Driver driver , boolean isAvailable);
    Page<RideDto> getAllMyRides(int pageNo);
    boolean existsByUsersId(Long userId);
    void updateRating(Long userId, Double rating);
    Driver onBoardNewDriver(Driver driver);
}
