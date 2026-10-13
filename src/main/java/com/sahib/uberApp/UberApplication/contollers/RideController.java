package com.sahib.uberApp.UberApplication.contollers;

import com.sahib.uberApp.UberApplication.dtos.RideRequestDto;
import com.sahib.uberApp.UberApplication.services.RiderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ride")
@RequiredArgsConstructor
public class RideController {

//    @Autowired
    private final RiderService riderService;

    @PostMapping("/requestRide")
    public RideRequestDto requestRide(@RequestBody RideRequestDto rideRequestDto){
        return riderService.requestRide(rideRequestDto);
    }

}
