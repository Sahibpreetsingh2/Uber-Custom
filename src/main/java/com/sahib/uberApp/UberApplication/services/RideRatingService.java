package com.sahib.uberApp.UberApplication.services;


import com.sahib.uberApp.UberApplication.dtos.RideRatingDto;
import com.sahib.uberApp.UberApplication.dtos.RideRatingResponseDto;

public interface RideRatingService {

    RideRatingResponseDto rate(Long rideId , RideRatingDto rideRatingDto);
}
