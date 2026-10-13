package com.sahib.uberApp.UberApplication.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RideRequestNotificationDto {

    private Long rideRequestId;

    private double pickupLatitude;

    private double pickupLongitude;

    private double dropLatitude;

    private double dropLongitude;

    private Double fare;

    private String paymentMethod;
}