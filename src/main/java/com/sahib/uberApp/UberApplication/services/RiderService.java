package com.sahib.uberApp.UberApplication.services;

import com.sahib.uberApp.UberApplication.dtos.RideDto;
import com.sahib.uberApp.UberApplication.dtos.RideRequestDto;
import com.sahib.uberApp.UberApplication.dtos.RiderDto;
import com.sahib.uberApp.UberApplication.entities.Rider;
import com.sahib.uberApp.UberApplication.entities.Users;
import org.springframework.data.domain.Page;

public interface RiderService {

    RideRequestDto requestRide(RideRequestDto rideRequestDto);
    RideDto cancelRide(Long rideId);
    RiderDto getMyProfile();
    Page<RideDto> getAllMyRides(int pageNo); // we can't pass rider id , because that comes from Spring Security :
    Rider getCurrentRider();
    Rider createNewRider(Users users);
    boolean existsByUsersId(Long userId);
    void updateRating(Long userId, Double rating);
}
