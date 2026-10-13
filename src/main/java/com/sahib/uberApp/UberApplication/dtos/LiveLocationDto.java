package com.sahib.uberApp.UberApplication.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LiveLocationDto {

    private long id;
    private String type;
    private double latitude;
    private double longitude;

}
