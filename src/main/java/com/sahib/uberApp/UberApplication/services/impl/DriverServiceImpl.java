package com.sahib.uberApp.UberApplication.services.impl;

import com.sahib.uberApp.UberApplication.dtos.DriverDto;
import com.sahib.uberApp.UberApplication.dtos.RideDto;
//import com.pisoft.uberApp.UberApplication.entities.*;
import com.sahib.uberApp.UberApplication.entities.*;
import com.sahib.uberApp.UberApplication.enums.PaymentStatus;
import com.sahib.uberApp.UberApplication.enums.RideRequestStatus;
import com.sahib.uberApp.UberApplication.enums.RideStatus;
import com.sahib.uberApp.UberApplication.exception.ResourceNotFound;
import com.sahib.uberApp.UberApplication.repositories.DriverRepository;
//import com.pisoft.uberApp.UberApplication.services.*;
import com.sahib.uberApp.UberApplication.services.*;
import com.sahib.uberApp.UberApplication.utils.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DriverServiceImpl implements DriverService {

    private static final int PAGE_NUMBER = 0;
    private final RideRequestService rideRequestService;
    private final DriverRepository driverRepository;
    private final RideService rideService;
    private final ModelMapper modelMapper;
    private final PaymentService paymentService;
    private final WalletService walletService;
    private final RedisService redisService;
    private final RedisGeoService redisGeoService;
    private final SimpMessagingTemplate messagingTemplate;

    @Value("${PAGE_SIZE}")
    Integer PAGE_SIZE;

    @Value("${MINIMUM_DRIVER_BALANCE}")
    Integer MINIMUM_DRIVER_BALANCE;

//    @Override
//    @Transactional
//    public RideDto acceptRide(Long rideRequestId) {
//
//         final Integer PAGE_NUMBER = 0;
//
//        RideRequest rideRequest = rideRequestService.findByRideRequestId(rideRequestId);
//
//        Driver currentDriver = getCurrentDriver();
//
//        Long userId = currentDriver.getUsers().getId();
//
//        Wallet driversWallet = walletService.findByUsers(userId);
//
//        // -70
//        // -50 ride not accept :
//
//        if (driversWallet.getBalance() < MINIMUM_DRIVER_BALANCE){
//            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE ,
//                    "Driver cannot accept the ride because of less than  "+MINIMUM_DRIVER_BALANCE);
//        }
//
//        if (!rideRequest.getRideRequestStatus().equals(RideRequestStatus.PENDING)){
//            throw new RuntimeException("Ride Request cannot be accpeted because it is not im pemding state");
//        }
//
//        if (!currentDriver.getAvailable()){
//            throw new RuntimeException("Driver is not available for this Ride");
//        }
//
//        Ride newRide = rideService.createNewRide(rideRequest, currentDriver);
//        return modelMapper.map(newRide , RideDto.class);
//    }
@Override
@Transactional
public RideDto acceptRide(Long rideRequestId) {

    Driver currentDriver = getCurrentDriver();

    RideRequest rideRequest =
            rideRequestService.findByRideRequestId(rideRequestId);

    if (!rideRequest.getRideRequestStatus()
            .equals(RideRequestStatus.PENDING)) {

        throw new RuntimeException(
                "Ride request cannot be accepted because it is not in PENDING state"
        );
    }

    if (!Boolean.TRUE.equals(currentDriver.getAvailable())) {
        throw new RuntimeException(
                "Driver is not available for this ride"
        );
    }

    Long userId = currentDriver.getUsers().getId();

    Wallet driversWallet =
            walletService.findByUsers(userId);

    if (driversWallet.getBalance() < MINIMUM_DRIVER_BALANCE) {
        throw new ResponseStatusException(
                HttpStatus.NOT_ACCEPTABLE,
                "Driver cannot accept the ride because balance is less than "
                        + MINIMUM_DRIVER_BALANCE
        );
    }

    Ride newRide =
            rideService.createNewRide(
                    rideRequest,
                    currentDriver
            );

    // Driver is now busy
    updateDriverAvailability(
            currentDriver,
            false
    );

    RideDto rideDto =
            modelMapper.map(
                    newRide,
                    RideDto.class
            );

    // Notify rider
    messagingTemplate.convertAndSend(
            "/topic/rider/"
                    + rideRequest.getRider().getId()
                    + "/ride-confirmed",
            rideDto
    );

    return rideDto;
}

    @Override
    public RideDto cancelRide(Long rideId) {
        return null;
    }

    @Override
    public RideDto startRide(Long rideId, String otp) {

        Driver currentDriver = getCurrentDriver();

        Ride ride = rideService.getById(rideId);

//        if (!ride.getDriver().equals(currentDriver)){
//            throw new RuntimeException("This Ride d'nt belongs to this Driver");
//        }
        System.out.println("==============================");
System.out.println("Ride Driver ID     : " + ride.getDriver().getId());
System.out.println("Current Driver ID  : " + currentDriver.getId());
System.out.println("==============================");
        if (!ride.getDriver().getId().equals(currentDriver.getId())) {
            throw new RuntimeException("This Ride doesn't belong to this Driver");
        }

//        if (!ride.getOtp().equals(otp)){
//            throw new RuntimeException("Otp did'not match");
//        }

        String redisOtp = (String) redisService.getString("ride:otp:" + rideId);

        if (redisOtp == null) {
            throw new RuntimeException("OTP has expired");
        }

        if (!redisOtp.equals(otp)) {
            throw new RuntimeException("Invalid OTP");
        }
        if (!ride.getRideStatus().equals(RideStatus.CONFIRMED)){
            throw new RuntimeException("You can't start Ride with these Ride Status ....");
        }

        ride.setStartedAt(LocalDateTime.now());
        Ride updateRide = rideService.updateRideStatus(ride, RideStatus.ONGOING);

        // driver availability = false :
        updateDriverAvailability(currentDriver , false);

        // creating object of payment :
        paymentService.createNewPayment(updateRide);

        return modelMapper.map(updateRide , RideDto.class);
    }

    @Override
    @Transactional
    public RideDto endRide(Long rideId) {

        Driver currentDriver = getCurrentDriver();

        Ride ride = rideService.getById(rideId);

        if (!ride.getDriver().equals(currentDriver)){
            throw new RuntimeException("This Ride doesn't belongs to this Driver");
        }

        if (!ride.getRideStatus().equals(RideStatus.ONGOING)){
            throw new RuntimeException("You can't end Ride with these Ride Status ....");
        }

        ride.setEndedAt(LocalDateTime.now());
        ride.setRideStatus(RideStatus.ENDED);
        Ride savedRide = rideService.saveRide(ride);

        // driver availability = true :
        updateDriverAvailability(currentDriver , true);

        Payment payment = paymentService.findByRide(savedRide);
        paymentService.updatePaymentStatus(payment , PaymentStatus.CONFIRMED);

        // process payment :
        paymentService.processPayment(savedRide , payment);

        // TODO Rating implementation :

        return modelMapper.map(savedRide, RideDto.class);
    }

    @Override
    public DriverDto getMyProfile() {
        return modelMapper.map(getCurrentDriver(), DriverDto.class);
    }

    @Override
    public Driver getCurrentDriver() {
        Users users = AuthUtils.getCurrentLoggedUser();
        return driverRepository.findById(users.getDriver().getId()).orElseThrow(()->
                new ResourceNotFound("Rider is not found with id"+users.getDriver().getId()));
    }

    @Override
    public void updateDriverAvailability(
            Driver driver,
            boolean isAvailable) {

        driver.setAvailable(isAvailable);

        driverRepository.save(driver);

        if (!isAvailable) {

            redisGeoService.removeDriver(
                    driver.getId()
            );
        }
    }

    @Override
    public Page<RideDto> getAllMyRides(int pageNo) {

        Pageable pageable = PageRequest.of(pageNo , PAGE_SIZE);

        Driver currentDriver = getCurrentDriver();
        return rideService.getAllRidesOfDriver(currentDriver, pageable).map((
                element) -> modelMapper.map(element, RideDto.class));
    }

    @Override
    public boolean existsByUsersId(Long userId) {
        return driverRepository.existsByUsersId(userId);
    }

    @Override
    public void updateRating(Long userId, Double rating) {
        driverRepository.updateRating(userId , rating);
    }

    @Override
    public Driver onBoardNewDriver(Driver driver) {
        return driverRepository.save(driver);
    }

}
