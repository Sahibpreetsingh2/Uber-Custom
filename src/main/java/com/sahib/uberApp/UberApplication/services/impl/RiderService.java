package com.sahib.uberApp.UberApplication.services.impl;

import com.sahib.uberApp.UberApplication.dtos.RideDto;
import com.sahib.uberApp.UberApplication.dtos.RideRequestDto;
import com.sahib.uberApp.UberApplication.dtos.RideRequestNotificationDto;
import com.sahib.uberApp.UberApplication.dtos.RiderDto;
import com.sahib.uberApp.UberApplication.entities.Driver;
import com.sahib.uberApp.UberApplication.entities.RideRequest;
import com.sahib.uberApp.UberApplication.entities.Rider;
import com.sahib.uberApp.UberApplication.entities.Users;
import com.sahib.uberApp.UberApplication.enums.RideRequestStatus;
import com.sahib.uberApp.UberApplication.exception.ResourceNotFound;
import com.sahib.uberApp.UberApplication.repositories.RideRequestRepository;
import com.sahib.uberApp.UberApplication.repositories.RiderRepository;
import com.sahib.uberApp.UberApplication.services.EmailService;
import com.sahib.uberApp.UberApplication.services.RideService;
import com.sahib.uberApp.UberApplication.strategies.RideStrategyManager;
import com.sahib.uberApp.UberApplication.utils.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RiderService implements com.sahib.uberApp.UberApplication.services.RiderService {

    private final ModelMapper modelMapper;
    private final RideRequestRepository rideRequestRepository;
    private final RiderRepository riderRepository;
    private final RideStrategyManager rideStrategyManager;
    private final RideService rideService;
    private final EmailService emailService;
    private final SimpMessagingTemplate messagingTemplate;

    @Value("${PAGE_SIZE}")
    Integer PAGE_SIZE;

    @Override
    public RideRequestDto requestRide(
            RideRequestDto rideRequestDto) {

        Rider currentRider = getCurrentRider();

        RideRequest rideRequest =
                modelMapper.map(
                        rideRequestDto,
                        RideRequest.class
                );

        rideRequest.setRideRequestStatus(
                RideRequestStatus.PENDING
        );

        rideRequest.setRider(currentRider);

        // Calculate fare
        Double fare =
                rideStrategyManager
                        .rideFareCalculationStrategy()
                        .calculateFare(rideRequest);

        rideRequest.setFare(fare);

        // Save request
        RideRequest savedRideRequest =
                rideRequestRepository.save(rideRequest);

        // Find nearby drivers
        List<Driver> drivers =
                rideStrategyManager
                        .findMatchingDriver(
                                currentRider.getAverageRating()
                        )
                        .matchDrivers(rideRequest);

        // Notify nearby drivers
        for (Driver driver : drivers) {

            RideRequestNotificationDto notification =
                    new RideRequestNotificationDto(

                            savedRideRequest.getId(),

                            rideRequest
                                    .getPickUpLocation()
                                    .getY(),

                            rideRequest
                                    .getPickUpLocation()
                                    .getX(),

                            rideRequest
                                    .getDropOffLocation()
                                    .getY(),

                            rideRequest
                                    .getDropOffLocation()
                                    .getX(),

                            rideRequest.getFare(),

                            rideRequest
                                    .getPaymentMethod()
                                    .name()
                    );

            messagingTemplate.convertAndSend(

                    "/topic/driver/"
                            + driver.getId()
                            + "/ride-requests",

                    notification
            );
            messagingTemplate.convertAndSend(
                    "/topic/rider/"
                            + rideRequest.getRider().getId()
                            + "/ride-confirmed",
                    notification
            );
        }

        return convertRideReqToRideReqDto(
                savedRideRequest
        );
    }

    @Override
    public RideDto cancelRide(Long rideId) {
        return null;
    }

    @Override
    public RiderDto getMyProfile() {
        Rider currentRider = getCurrentRider();
        return modelMapper.map(currentRider, RiderDto.class);
    }

    @Override
    public Page<RideDto> getAllMyRides(int pageNo) {

        Rider currentRider = getCurrentRider();
        Pageable pageable = PageRequest.of(pageNo , PAGE_SIZE);

        return rideService.getAllRidesOfRider(currentRider, pageable).map((
                element) -> modelMapper.map(element, RideDto.class));
    }



    @Override
    public Rider getCurrentRider() {
        Users users = AuthUtils.getCurrentLoggedUser();
        return riderRepository.findById(users.getRider().getId()).orElseThrow(()->
                new ResourceNotFound("Rider is not found with id"+users.getRider().getId()));
    }

    @Override
    public Rider createNewRider(Users users) {
        Rider rider = Rider.builder()
                .users(users)
                .averageRating(0.0)
                .build();
        return riderRepository.save(rider);
    }

    @Override
    public boolean existsByUsersId(Long userId) {
        return riderRepository.existsByUsersId(userId);
    }

    @Override
    @Transactional
    public void updateRating(Long userId, Double rating) {
        riderRepository.updateRating(userId , rating);
    }

    public RideRequestDto convertRideReqToRideReqDto (RideRequest rideRequest){
        return modelMapper.map(rideRequest, RideRequestDto.class);
    }

    public RideRequest convertRideReqDtoToRideReq(RideRequestDto rideRequestDto){
        return modelMapper.map(rideRequestDto, RideRequest.class);
    }

}
