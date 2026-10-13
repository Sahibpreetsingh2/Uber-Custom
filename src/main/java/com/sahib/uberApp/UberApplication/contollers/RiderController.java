package com.sahib.uberApp.UberApplication.contollers;

import com.sahib.uberApp.UberApplication.dtos.RideDto;
import com.sahib.uberApp.UberApplication.dtos.RiderDto;
import com.sahib.uberApp.UberApplication.services.RiderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rider")
@RequiredArgsConstructor
public class RiderController {

    private final RiderService riderService;

    @GetMapping("/getAllMyRides/{pageNo}")
    public List<RideDto> getAllMyRides(@RequestParam(defaultValue = "0") int pageNo){
        return riderService.getAllMyRides(pageNo).getContent();
    }

    @GetMapping("/getMyProfile")
    public RiderDto getMyProfile(){
        return riderService.getMyProfile();
    }
}
