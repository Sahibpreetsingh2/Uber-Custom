package com.sahib.uberApp.UberApplication.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RideRatingDto {

    private Double rating;
    private String feedback;
}
