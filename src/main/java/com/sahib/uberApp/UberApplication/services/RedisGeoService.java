package com.sahib.uberApp.UberApplication.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.domain.geo.GeoReference;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RedisGeoService {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String DRIVER_LOCATION_KEY =
            "driver:locations";


    // 1. Add/update driver's current location
    public void updateDriverLocation(
            Long driverId,
            double latitude,
            double longitude) {

        redisTemplate.opsForGeo().add(
                DRIVER_LOCATION_KEY,

                // IMPORTANT:
                // Point = longitude, latitude
                new Point(longitude, latitude),

                driverId.toString()
        );
    }


    // 2. Remove driver from live location tracking
    public void removeDriver(Long driverId) {

        redisTemplate.opsForZSet().remove(
                DRIVER_LOCATION_KEY,
                driverId.toString()
        );
    }


    // 3. Find drivers within radius
    public List<Long> findNearbyDrivers(
            double latitude,
            double longitude,
            double radiusKm) {

        GeoResults<RedisGeoCommands.GeoLocation<Object>> results =
                redisTemplate.opsForGeo().search(

                        DRIVER_LOCATION_KEY,

                        GeoReference.fromCoordinate(
                                longitude,
                                latitude
                        ),

                        new Distance(
                                radiusKm,
                                Metrics.KILOMETERS
                        ),

                        RedisGeoCommands.GeoSearchCommandArgs
                                .newGeoSearchArgs()
                                .includeDistance()
                                .sortAscending()
                );

        List<Long> driverIds = new ArrayList<>();

        if (results == null) {
            return driverIds;
        }

        for (
                GeoResult<RedisGeoCommands.GeoLocation<Object>> result
                : results
        ) {

            driverIds.add(
                    Long.valueOf(
                            result.getContent()
                                    .getName()
                                    .toString()
                    )
            );
        }

        return driverIds;
    }
}