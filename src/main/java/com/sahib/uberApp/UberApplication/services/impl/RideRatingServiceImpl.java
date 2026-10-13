package com.sahib.uberApp.UberApplication.services.impl;

import com.sahib.uberApp.UberApplication.dtos.RideRatingDto;
import com.sahib.uberApp.UberApplication.dtos.RideRatingResponseDto;
import com.sahib.uberApp.UberApplication.entities.Ride;
import com.sahib.uberApp.UberApplication.entities.RideRating;
import com.sahib.uberApp.UberApplication.entities.Users;
import com.sahib.uberApp.UberApplication.enums.RideStatus;
import com.sahib.uberApp.UberApplication.repositories.RideRatingRepository;
//import com.pisoft.uberApp.UberApplication.services.*;
import com.sahib.uberApp.UberApplication.services.DriverService;
import com.sahib.uberApp.UberApplication.services.RideRatingService;
import com.sahib.uberApp.UberApplication.services.RideService;
import com.sahib.uberApp.UberApplication.services.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class RideRatingServiceImpl implements RideRatingService {

    private final UserService userService;
    private final RideService rideService;
    private final RideRatingRepository rideRatingRepository;
    private final RiderService riderService;
    private final DriverService driverService;
    private final ModelMapper modelMapper;

    @Override
    public RideRatingResponseDto rate(Long rideId, RideRatingDto rideRatingDto) {

        // get current logged use :

        Users currentLoggedUser = userService.getCurrentLoggedUser();  // 1

        Ride ride = rideService.getById(rideId);

        if (!ride.getRideStatus().equals(RideStatus.ENDED)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST ,
                    "User cannot rate this ride because it is not ENDED");
        }

        if (rideRatingDto == null || rideRatingDto.getRating() < 1 || rideRatingDto.getRating() > 5){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST , " Ride Rating b/w 1 to 5");
        }

        boolean exists = rideRatingRepository.existsByRideIdAndRaterId(rideId, currentLoggedUser.getId());

        if (exists){
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE , "User already rated this ride ");
        }

        boolean isDriver = currentLoggedUser.getId().equals(ride.getDriver().getUsers().getId());
        boolean isRider = currentLoggedUser.getId().equals(ride.getRider().getUsers().getId());

        if (!isDriver && !isRider){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"you are not part of this ride");
        }

        Users rated = isDriver ? ride.getRider().getUsers() : ride.getDriver().getUsers();
            // 1  !=  12
        if (currentLoggedUser.getId().equals(rated.getId())){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN , "you cannot rate yourself ");
        }

        // save rating :

        RideRating rideRating = saveRating(currentLoggedUser, rated, rideRatingDto, ride);

        //  update Average Rating as well as rating count :

        //  formula : newAvg = (oldAvg * oldCount + newRating) / (oldCount + 1)

        updateRatingAtomic(rated.getId() , rideRating.getRating());


        return modelMapper.map(rideRating, RideRatingResponseDto.class);
    }

    public void updateRatingAtomic(Long userId, Double rating) {

        boolean isRider = riderService.existsByUsersId(userId);
        if (isRider){
            riderService.updateRating(userId , rating);
            return;
        }

        boolean isDriver = driverService.existsByUsersId(userId);
        
        if (isDriver){
            driverService.updateRating(userId , rating);
            return;
        }
        
        throw new ResponseStatusException(HttpStatus.FORBIDDEN , "User is not a RIDER or DRIVER");

    }

    public RideRating saveRating(Users currentLoggedUser, Users rated, RideRatingDto rideRatingDto, Ride ride) {
        RideRating rideRating = RideRating.builder()
                .rating(rideRatingDto.getRating())
                .feedback(rideRatingDto.getFeedback())
                .rater(currentLoggedUser)
                .rated(rated)
                .ride(ride)
                .build();

        return rideRatingRepository.save(rideRating);

    }
}
