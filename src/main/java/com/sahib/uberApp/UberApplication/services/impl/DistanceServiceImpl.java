package com.sahib.uberApp.UberApplication.services.impl;

import com.sahib.uberApp.UberApplication.services.DistanceService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class DistanceServiceImpl implements DistanceService {

    @Override
    public double calculateDistance(Point src, Point dest) {
       // TODO  OSRM API use for calculating the distance :

        String coordinates  = src.getX() + ","+src.getY()+";"+dest.getX()+","+dest.getY();

        String OSRM_BASE_URL = "https://router.project-osrm.org/route/v1/driving/?overview=false";
        OSRMResponseDto osrmResponseDto = RestClient.builder()
                .baseUrl(OSRM_BASE_URL)
                .build()
                .get()
                .uri(coordinates)
                .retrieve()
                .body(OSRMResponseDto.class);

        System.out.println("distance : "+osrmResponseDto.getRoutes().get(0).getDistance() / 1000);
        return osrmResponseDto.getRoutes().get(0).getDistance() / 1000.0;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class OSRMResponseDto{
        List<OSRMRoutes> routes;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class OSRMRoutes{
        private double distance;
    }
}
