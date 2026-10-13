package com.sahib.uberApp.UberApplication.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationUpdateDto {

    private double  longitude;
    private double latitude;

}
