package com.sahib.uberApp.UberApplication.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RideConfirmedDto {

    private Long rideId;

    private Long rideRequestId;

    private Long driverId;

    private Long riderId;

    private String message;
}