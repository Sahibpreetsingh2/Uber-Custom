package com.sahib.uberApp.UberApplication.services.impl;

import com.sahib.uberApp.UberApplication.entities.RideRequest;
import com.sahib.uberApp.UberApplication.exception.ResourceNotFound;
import com.sahib.uberApp.UberApplication.repositories.RideRequestRepository;
import com.sahib.uberApp.UberApplication.services.RideRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RideRequestServiceImpl implements RideRequestService {

    private final RideRequestRepository rideRequestRepository;

    @Override
    public RideRequest findByRideRequestId(Long rideRequestId) {
        return rideRequestRepository.findById(rideRequestId).orElseThrow(()->
                new ResourceNotFound("Ride Request Id not found"));
    }
}
