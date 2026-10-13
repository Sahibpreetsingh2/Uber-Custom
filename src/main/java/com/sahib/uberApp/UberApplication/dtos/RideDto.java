package com.sahib.uberApp.UberApplication.dtos;

import com.sahib.uberApp.UberApplication.enums.PaymentMethod;
import com.sahib.uberApp.UberApplication.enums.RideStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RideDto {

    private Long id;

    private PointDto pickUpLocation;

    private PointDto dropOffLocation;

    private LocalDateTime createdTime;

    private RiderDto rider;

    private DriverDto driver;

    private PaymentMethod paymentMethod;

    private RideStatus rideStatus;

    private String otp;

    LocalDateTime startedAt;
    LocalDateTime endedAt;
    private Double fare;
}
