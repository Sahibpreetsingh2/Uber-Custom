package com.sahib.uberApp.UberApplication.repositories;

import com.sahib.uberApp.UberApplication.entities.RideRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RideRatingRepository extends JpaRepository<RideRating, Long> {


    boolean existsByRideIdAndRaterId(Long rideId, Long userId);

}
