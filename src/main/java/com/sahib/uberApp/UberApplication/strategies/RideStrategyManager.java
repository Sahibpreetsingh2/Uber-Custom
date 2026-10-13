package com.sahib.uberApp.UberApplication.strategies;

import com.sahib.uberApp.UberApplication.strategies.impl.DriverMatchingHighestRatedDriverStrategy;
import com.sahib.uberApp.UberApplication.strategies.impl.DriverMatchingNearestDriverStrategy;
import com.sahib.uberApp.UberApplication.strategies.impl.RideFareDefaultCalculationStrategy;
import com.sahib.uberApp.UberApplication.strategies.impl.RideFareSurgePricingStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
@RequiredArgsConstructor
public class RideStrategyManager {

    private final DriverMatchingNearestDriverStrategy driverMatchingNearestDriverStartegy;
    private final DriverMatchingHighestRatedDriverStrategy driverMatchingHighestRatedDriverStartegy;


    private final RideFareDefaultCalculationStrategy rideFareDefaultCalculationStrategy;
    private final RideFareSurgePricingStrategy rideFareSurgePricingStrategy;

    public DriverMatchingStrategy findMatchingDriver(double rating){

        if (rating >= 4){
            // high
            return  driverMatchingHighestRatedDriverStartegy;
        }else{
            // near
            return driverMatchingNearestDriverStartegy;
        }
    }

    public RideFareCalculationStrategy rideFareCalculationStrategy(){

        //  6PM - 9PM ( price 20 )

        LocalTime surgeStartTime = LocalTime.of(18 , 0); // 6pm
        LocalTime surgeEndTime = LocalTime.of(21 , 0);  // 9pm


        LocalTime currentTime = LocalTime.now();

        boolean isSurgeTime = currentTime.isAfter(surgeStartTime) && currentTime.isBefore(surgeEndTime);

        if (isSurgeTime){
            // surge pricing
            return rideFareSurgePricingStrategy;
        }else {
            // default
            return rideFareDefaultCalculationStrategy;
        }
    }
}
