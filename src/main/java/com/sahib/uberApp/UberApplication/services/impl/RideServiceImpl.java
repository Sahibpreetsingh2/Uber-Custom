package com.sahib.uberApp.UberApplication.services.impl;

import com.sahib.uberApp.UberApplication.dtos.RideDto;
import com.sahib.uberApp.UberApplication.entities.Driver;
import com.sahib.uberApp.UberApplication.entities.Ride;
import com.sahib.uberApp.UberApplication.entities.RideRequest;
import com.sahib.uberApp.UberApplication.entities.Rider;
import com.sahib.uberApp.UberApplication.enums.RideRequestStatus;
import com.sahib.uberApp.UberApplication.enums.RideStatus;
import com.sahib.uberApp.UberApplication.exception.ResourceNotFound;
import com.sahib.uberApp.UberApplication.repositories.DriverRepository;
import com.sahib.uberApp.UberApplication.repositories.RideRepository;
import com.sahib.uberApp.UberApplication.services.RedisService;
import com.sahib.uberApp.UberApplication.services.RideService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class RideServiceImpl implements RideService {

    private final ModelMapper modelMapper;
    private final RideRepository rideRepository;
    private final DriverRepository driverRepository;
    private final RedisService redisService;

    @Override
    public Ride getById(Long rideId) {
        return rideRepository.findById(rideId).orElseThrow(()-> new ResourceNotFound("Ride not found with id : "+rideId));
    }

    @Override
    @Transactional
    public Ride createNewRide(RideRequest rideRequest, Driver driver) {

        rideRequest.setRideRequestStatus(RideRequestStatus.CONFIRMED); // dirty checking

        Ride ride = modelMapper.map(rideRequest, Ride.class);
        ride.setDriver(driver);
        ride.setId(null);
        ride.setRideStatus(RideStatus.CONFIRMED);
        Ride savedRide =  rideRepository.save(ride);
        String generated = generateOTP();
//        ride.setOtp(generated);


        redisService.set(
                "ride:otp:" + savedRide.getId(),
                generated,
                300L
        );
        return savedRide;
    }


    @Override
    public Ride updateRideStatus(Ride ride, RideStatus rideStatus) {
        ride.setRideStatus(rideStatus);
        return rideRepository.save(ride);
    }

    @Override
    public Page<Ride> getAllRidesOfRider(Rider rider, Pageable pageable) {
        return rideRepository.findByRider(rider , pageable);
    }

    @Override
    public Page<Ride> getAllRidesOfDriver(Driver driver, Pageable pageable) {
        return rideRepository.findByDriver(driver , pageable);
    }

    @Override
    public Ride saveRide(Ride ride) {
        return rideRepository.save(ride);
    }


    public List<RideDto> findAllRidesOfDriver() {

        Driver byDriverId = driverRepository.findById(1L).orElse(null);


        List<Ride> allRidesOfDriver = rideRepository.findAllRidesOfDriver(byDriverId.getId());
        return allRidesOfDriver.stream().map((element) -> modelMapper.map(element, RideDto.class))
                .toList();
    }

    public String generateOTP(){
        Random random = new Random();
        int nestedInt = random.nextInt(1000);
        return  String.format("%04d" , nestedInt);

    }
}
